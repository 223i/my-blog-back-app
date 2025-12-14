package com.iron.repository;

import com.iron.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
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
    public List<Post> findPostsForPage(String postTitle, int pageNumber, int pageSize) {
        StringBuilder sql = new StringBuilder("SELECT id, title, text, likesCount, commentsCount FROM posts");


        List<Object> params = new ArrayList<>();
        if (postTitle != null && !postTitle.isBlank()) {
            sql.append(" WHERE title LIKE ?");
            params.add("%" + postTitle + "%");
        }
        sql.append(" ORDER BY id DESC LIMIT ? OFFSET ?");
        int offset = (pageNumber - 1) * pageSize;
        params.add(pageSize);
        params.add(offset);

        // Получаем список постов
        List<Post> posts = jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> {
            Post post = new Post(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("text"),
                    new ArrayList<>(),
                    rs.getInt("likesCount"),
                    rs.getInt("commentsCount")
            );
            return post;
        });

        if (!posts.isEmpty()) {
            // Получаем теги для всех постов за один запрос
            List<Integer> postIds = posts.stream().map(Post::getId).toList();
            String tagsSql = "SELECT pt.post_id, t.text " +
                    "FROM tags t " +
                    "JOIN post_tag pt ON t.id = pt.tag_id " +
                    "WHERE pt.post_id IN (" +
                    postIds.stream().map(id -> "?").collect(Collectors.joining(",")) +
                    ")";
            Object[] tagParams = postIds.toArray();

            Map<Integer, List<String>> tagsMap = new HashMap<>();
            jdbcTemplate.query(tagsSql, (rs) -> {
                int postId = rs.getInt("post_id");
                String tag = rs.getString("text");
                tagsMap.computeIfAbsent(postId, k -> new ArrayList<>()).add(tag);
            }, tagParams);

            // Присваиваем теги постам
            posts.forEach(post -> post.setTags(tagsMap.getOrDefault(post.getId(), new ArrayList<>())));
        }

        return posts;
    }

    @Override
    public Long countPosts(String postTitle) {

        if (postTitle != null && !postTitle.isEmpty()) {
            String sql = "SELECT COUNT(*) FROM posts WHERE title LIKE ?";
            return jdbcTemplate.queryForObject(sql, Long.class, "%" + postTitle + "%");
        } else {
            String sql = "SELECT COUNT(*) FROM posts";
            return jdbcTemplate.queryForObject(sql, Long.class); // без параметров
        }
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

    @Override
    public Post save(Post post) {

        List<Integer> tagsIds = new ArrayList<>();
        post.getTags()
                .forEach(
                        tag -> {
                            String sql = "INSERT INTO tags(text) values (?)";
                            KeyHolder keyHolder = new GeneratedKeyHolder();

                            jdbcTemplate.update(connection -> {
                                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                ps.setString(1, tag);
                                return ps;
                            }, keyHolder);
                            tagsIds.add(keyHolder.getKey().intValue());
                        });

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO posts(title, text, likesCount, commentsCount) values(?, ?, 0, 0)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            return ps;
        }, keyHolder);
        int postId = keyHolder.getKey().intValue();
        tagsIds.forEach(tagId -> jdbcTemplate.update("INSERT INTO post_tag(post_id, tag_id) values (?, ?)", postId, tagId));
        post.setId(postId);
        post.setLikesCount(0);
        post.setCommentsCount(0);
        return post;
    }

    @Override
    public void update(Post post) {
        String sqlPost = "UPDATE posts SET title = ?, text = ?, likesCount = ?, commentsCount = ? WHERE id = ?";
        jdbcTemplate.update(sqlPost,
                post.getTitle(),
                post.getText(),
                post.getLikesCount() != null ? post.getLikesCount() : 0,
                post.getCommentsCount() != null ? post.getCommentsCount() : 0,
                post.getId());

        List<Integer> existingTagIds = jdbcTemplate.queryForList(
                "SELECT tag_id FROM post_tag WHERE post_id = ?",
                Integer.class,
                post.getId()
        );

        if (!existingTagIds.isEmpty()) {
            jdbcTemplate.update("DELETE FROM post_tag WHERE post_id = ?", post.getId());
        }

        if (post.getTags() != null && !post.getTags().isEmpty()) {
            List<Integer> newTagIds = new ArrayList<>();
            for (String tag : post.getTags()) {
                // Проверяем, есть ли тег в таблице tags
                Integer tagId = jdbcTemplate.query(
                        "SELECT id FROM tags WHERE text = ?",
                        ps -> ps.setString(1, tag),
                        rs -> rs.next() ? rs.getInt("id") : null
                );

                // Если тега нет, вставляем новый
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

                newTagIds.add(tagId);
            }

            for (Integer tagId : newTagIds) {
                jdbcTemplate.update(
                        "MERGE INTO post_tag(post_id, tag_id) KEY(post_id, tag_id) VALUES (?, ?)",
                        post.getId(),
                        tagId
                );
            }
        }

        post.setLikesCount(post.getLikesCount() != null ? post.getLikesCount() : 0);
        post.setCommentsCount(post.getCommentsCount() != null ? post.getCommentsCount() : 0);
    }

    @Override
    public void deleteById(Integer id) {
        jdbcTemplate.update("delete from post_tag where post_id = ?", id);
        jdbcTemplate.update("delete from posts where id = ?", id);
    }
}
