package com.iron.repository;

import com.iron.model.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class CommentDaoRepositoryImpl implements CommentDaoRepository {

    private final JdbcTemplate jdbcTemplate;

    public CommentDaoRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Comment> findAll(Integer post_id) {
        List<Comment> comments = jdbcTemplate.query("SELECT id, post_id, text FROM comments WHERE post_id = ?",
                (rs, rowNum) -> new Comment(
                        rs.getInt("id"),
                        rs.getInt("post_id"),
                        rs.getString("text")),
                post_id);
        return comments;
    }

    @Override
    public Comment findCommentById(Integer post_id, Integer id) {

        var rs = jdbcTemplate.queryForRowSet(
                "SELECT id, text, post_id FROM comments WHERE post_id = ? AND id = ?",
                post_id, id);
        if (!rs.next()) {
            throw new NoSuchElementException(String.format("Comment with id=%s for post_id=%s not found", id, post_id));
        } else {
            return new Comment(
                    rs.getInt("id"),
                    rs.getInt("post_id"),
                    rs.getString("text"));
        }
    }

    @Override
    public Comment save(Integer post_id, Comment comment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO comments(post_id, text) values(?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, String.valueOf(post_id));
            ps.setString(2, String.valueOf(comment.getText()));
            return ps;
        }, keyHolder);
        comment.setId(keyHolder.getKey().intValue());
        return comment;
    }

    @Override
    public Comment update(Integer post_id, Comment comment) {
        String sqlPost = "UPDATE comments SET text = ? WHERE id = ? AND post_id = ?";
        int rows = jdbcTemplate.update(sqlPost,
                comment.getText(),
                comment.getId(),
                post_id);
        if (rows == 0) {
            throw new NoSuchElementException(
                    "Comment with id=%s for post_id=%s not found".formatted(comment.getId(), post_id)
            );
        }
        return comment;
    }

    @Override
    public void deleteById(Integer post_id, Integer id) {
        jdbcTemplate.update("DELETE FROM comments where id = ? AND post_id = ?", id, post_id);
    }
}
