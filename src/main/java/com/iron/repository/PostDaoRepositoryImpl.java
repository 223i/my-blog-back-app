package com.iron.repository;

import com.iron.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostDaoRepositoryImpl implements PostDaoRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostDaoRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll() {
        List<Post> allPosts = jdbcTemplate.query("SELECT id, title, text, likesCount, commentsCount from posts",
                (rs, rowNum) -> new Post(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        new ArrayList<>(),
                        rs.getInt("likesCount"),
                        rs.getInt("commentsCount")
                ));

        allPosts.forEach(post -> jdbcTemplate.query("SELECT pt.post_id, t.id, t.text\n" +
                        "FROM tags t\n" +
                        "JOIN post_tag pt ON t.id = pt.tag_id",
                (rs, rowNum) -> post.getTags().add(rs.getString("text"))));
        return allPosts;
    }

    @Override
    public Post findPostById(Integer id) {
        var rs = jdbcTemplate.queryForRowSet(
                "SELECT id, title, text, likesCount, commentsCount FROM posts WHERE id = ?",
                id);
        if (!rs.next()) {
            throw null; // TODO: добавить throw new NotFoundException(...)
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
            PreparedStatement ps = connection.prepareStatement("INSERT INTO posts(title, text) values(?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            return ps;
        }, keyHolder);
        int postId = keyHolder.getKey().intValue();
        tagsIds.forEach(tagId -> jdbcTemplate.update("INSERT INTO post_tag(post_id, tag_id) values (?, ?)", postId, tagId));
        post.setId(postId);
        return post;
    }
}
