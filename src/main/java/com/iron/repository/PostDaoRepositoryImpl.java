package com.iron.repository;

import com.iron.model.Post;
import com.iron.model.PostSearchCriteria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PostDaoRepositoryImpl implements PostDaoRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostDaoRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findPostsForPage(PostSearchCriteria criteria, int pageNumber, int pageSize) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT p.id, p.title, p.text, p.likesCount, p.commentsCount FROM posts p ");
        List<Object> params = new ArrayList<>();

        appendTagJoinIfNeeded(sql, criteria);
        appendWhereConditions(sql, params, criteria);

        sql.append(" ORDER BY p.id DESC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((pageNumber - 1) * pageSize);

        List<Post> posts = jdbcTemplate.query(sql.toString(), params.toArray(), this::mapPost);

        if (!posts.isEmpty() && criteria.getTags() != null && !criteria.getTags().isEmpty()) {
            populateTagsForPosts(posts);
        }

        return posts;
    }

    private Post mapPost(ResultSet rs, int rowNum) throws SQLException {
        return new Post(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("text"),
                new ArrayList<>(), // теги будут заполнены позже
                rs.getInt("likesCount"),
                rs.getInt("commentsCount")
        );
    }

    private void populateTagsForPosts(List<Post> posts) {
        List<Integer> postIds = posts.stream().map(Post::getId).toList();
        if (postIds.isEmpty()) return;

        String sql = "SELECT pt.post_id, t.text " +
                "FROM tags t JOIN post_tag pt ON t.id = pt.tag_id " +
                "WHERE pt.post_id IN (" +
                postIds.stream().map(id -> "?").collect(Collectors.joining(",")) +
                ")";

        Object[] params = postIds.toArray();
        Map<Integer, List<String>> tagsMap = new HashMap<>();

        jdbcTemplate.query(sql, params, rs -> {
            int postId = rs.getInt("post_id");
            String tag = rs.getString("text");
            tagsMap.computeIfAbsent(postId, k -> new ArrayList<>()).add(tag);
        });

        posts.forEach(post -> post.setTags(tagsMap.getOrDefault(post.getId(), new ArrayList<>())));
    }


    @Override
    public Long countPosts(PostSearchCriteria criteria) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT p.id) FROM posts p ");
        List<Object> params = new ArrayList<>();

        appendTagJoinIfNeeded(sql, criteria);
        appendWhereConditions(sql, params, criteria);

        return jdbcTemplate.queryForObject(sql.toString(), Long.class, params.toArray());
    }

    @Override
    public Post findPostById(Integer id) {
        var rs = jdbcTemplate.queryForRowSet(
                "SELECT id, title, text, likesCount, commentsCount FROM posts WHERE id = ?",
                id);
        if (!rs.next()) {
            throw new NoSuchElementException(String.format("Post with id=%s not found", id));
        } else {

            Post post = new Post(rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("text"),
                    new ArrayList<>(),
                    rs.getInt("likesCount"),
                    rs.getInt("commentsCount"));

            jdbcTemplate.query(
                    "SELECT pt.post_id AS post_id, " +
                            "       t.id       AS tag_id, " +
                            "       t.text     AS tag_text " +
                            "FROM tags t " +
                            "JOIN post_tag pt ON t.id = pt.tag_id " +
                            "WHERE pt.post_id = ?",
                    new Object[]{id},
                    (resset, rowNum) -> resset.getString("tag_text")
            ).forEach(post.getTags()::add);

            return post;
        }
    }

    @Transactional
    @Override
    public Post save(Post post) {
        List<Integer> tagIds = saveTags(post.getTags());

        int postId = savePost(post);
        post.setId(postId);
        post.setLikesCount(0);
        post.setCommentsCount(0);

        savePostTagConnections(postId, tagIds);

        return post;
    }

    @Override
    @Transactional
    public void update(Post post) {

        String sqlPost = "UPDATE posts SET title = ?, text = ?, likesCount = ?, commentsCount = ? WHERE id = ?";
        jdbcTemplate.update(sqlPost,
                post.getTitle(),
                post.getText(),
                safeCount(post.getLikesCount()),
                safeCount(post.getCommentsCount()),
                post.getId());

        updatePostTags(post);
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        jdbcTemplate.update("delete from post_tag where post_id = ?", id);
        jdbcTemplate.update("delete from posts where id = ?", id);
    }

    private int savePost(Post post) {
        String sql = "INSERT INTO posts(title, text, likesCount, commentsCount) values(?, ?, 0, 0)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    private List<Integer> saveTags(List<String> tags) {
        String sql = "INSERT INTO tags(text) values (?)";
        List<Integer> tagIds = new ArrayList<>();

        for (String tag : tags) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, tag);
                return ps;
            }, keyHolder);
            tagIds.add(keyHolder.getKey().intValue());
        }
        return tagIds;
    }

    private void savePostTagConnections(int postId, List<Integer> tagIds) {
        String sql = "INSERT INTO post_tag(post_id, tag_id) values (?, ?)";
        jdbcTemplate.batchUpdate(sql, tagIds, tagIds.size(),
                (ps, tagId) -> {
                    ps.setInt(1, postId);
                    ps.setInt(2, tagId);
                });
    }

    private int safeCount(Integer value) {
        return value != null ? value : 0;
    }

    private void updatePostTags(Post post) {
        Integer postId = post.getId();

        // Удаляем существующие связи
        jdbcTemplate.update("DELETE FROM post_tag WHERE post_id = ?", postId);

        if (post.getTags() == null || post.getTags().isEmpty()) {
            return;
        }

        // Получаем или создаем все теги и собираем их ID
        List<Integer> tagIds = post.getTags().stream()
                .map(this::getOrCreateTagId)
                .toList();

        // Вставляем новые связи
        tagIds.forEach(tagId ->
                jdbcTemplate.update(
                        "MERGE INTO post_tag(post_id, tag_id) KEY(post_id, tag_id) VALUES (?, ?)",
                        postId,
                        tagId
                )
        );
    }

    private Integer getOrCreateTagId(String tag) {
        Integer tagId = jdbcTemplate.query(
                "SELECT id FROM tags WHERE text = ?",
                ps -> ps.setString(1, tag),
                rs -> rs.next() ? rs.getInt("id") : null
        );

        if (tagId == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO tags(text) VALUES (?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, tag);
                return ps;
            }, keyHolder);
            tagId = keyHolder.getKey().intValue();
        }
        return tagId;
    }

    private void appendTagJoinIfNeeded(StringBuilder sql, PostSearchCriteria criteria) {
        if (criteria.getTags() != null && !criteria.getTags().isEmpty()) {
            sql.append("JOIN post_tag pt ON p.id = pt.post_id ")
                    .append("JOIN tags t ON pt.tag_id = t.id ");
        }
    }

    private void appendWhereConditions(StringBuilder sql, List<Object> params, PostSearchCriteria criteria) {
        List<String> conditions = new ArrayList<>();

        String titleCondition = buildTitleCondition(criteria.getTitleSubstring(), params);
        if (titleCondition != null) {
            conditions.add(titleCondition);
        }

        String tagsCondition = buildTagsCondition(criteria.getTags(), params);
        if (tagsCondition != null) {
            conditions.add(tagsCondition);
        }

        if (!conditions.isEmpty()) {
            sql.append("WHERE ").append(String.join(" AND ", conditions));
        }
    }

    private String buildTitleCondition(String titleSubstring, List<Object> params) {
        if (titleSubstring != null && !titleSubstring.isBlank()) {
            params.add("%" + titleSubstring + "%");
            return "p.title LIKE ?";
        }
        return null;
    }

    private String buildTagsCondition(List<String> tags, List<Object> params) {
        if (tags != null && !tags.isEmpty()) {
            String placeholders = tags.stream().map(tag -> "?").collect(Collectors.joining(", "));
            params.addAll(tags);
            return "t.text IN (" + placeholders + ")";
        }
        return null;
    }
}
