package com.iron.repository;

import com.iron.model.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(CommentDaoRepositoryImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentDaoRepositoryImplTest {

    @Autowired
    private CommentDaoRepositoryImpl repository;

    @Test
    void shouldFindAllCommentsForPost() {
        List<Comment> comments = repository.findAll(1);

        assertEquals(2, comments.size());
        assertTrue(comments.stream().allMatch(c -> c.getPostId() == 1));
    }

    @Test
    void shouldFindCommentById() {
        Comment comment = repository.findCommentById(1, 1);

        assertNotNull(comment);
        assertEquals(1, comment.getPostId());
        assertEquals("Отличное объяснение, стало намного понятнее", comment.getText());
    }

    @Test
    void shouldThrowExceptionIfCommentNotFound() {
        assertThrows(
                NoSuchElementException.class,
                () -> repository.findCommentById(1, 999)
        );
    }

    @Test
    void shouldSaveComment() {
        Comment comment = new Comment(null, 1, "new comment");

        Comment saved = repository.save(1, comment);

        assertNotNull(saved.getId());

        Comment fromDb = repository.findCommentById(1, saved.getId());
        assertEquals("new comment", fromDb.getText());
    }

    @Test
    void shouldUpdateComment() {
        Comment comment = repository.findCommentById(1, 1);
        comment.setText("updated");

        repository.update(1, comment);

        Comment updated = repository.findCommentById(1, 1);
        assertEquals("updated", updated.getText());
    }

    @Test
    void shouldThrowExceptionOnUpdateIfNotFound() {
        Comment comment = new Comment(999, 1, "text");

        assertThrows(
                NoSuchElementException.class,
                () -> repository.update(1, comment)
        );
    }

    @Test
    void shouldDeleteComment() {
        repository.deleteById(1, 1);

        assertThrows(
                NoSuchElementException.class,
                () -> repository.findCommentById(1, 1)
        );
    }
}
