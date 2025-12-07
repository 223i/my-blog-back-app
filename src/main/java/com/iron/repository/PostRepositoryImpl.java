package com.iron.repository;

import com.iron.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostRepositoryImpl(JdbcTemplate jdbcTemplate) {
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
        }

        return new Post(rs.getInt("id"),
                rs.getString("title"),
                rs.getString("text"),
                new ArrayList<>(),
                rs.getInt("likesCount"),
                rs.getInt("commentsCount"));
    }
}
