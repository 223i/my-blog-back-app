package com.iron.unit.test;

import com.iron.dto.comment.CommentCreateDto;
import com.iron.dto.comment.CommentUpdateDto;
import com.iron.mapper.CommentDtoMapper;
import com.iron.model.Comment;
import com.iron.repository.CommentDaoRepository;
import com.iron.service.CommentService;
import com.iron.unit.test.configuration.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class CommentServiceTest {

    @Autowired
    private CommentDaoRepository commentDaoRepository;

    @Autowired
    private CommentDtoMapper commentDtoMapper;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    void resetMocks() {
        reset(commentDaoRepository);
    }


    @Test
    void shouldReturnAllCommentsForPost() {
        String postId = "1";
        List<Comment> comments = List.of(
                new Comment(1, 1, "text1"),
                new Comment(2, 1, "text2")
        );

        when(commentDaoRepository.findAll(1)).thenReturn(comments);

        List<Comment> result = commentService.findAll(postId);

        assertEquals(comments, result);
        verify(commentDaoRepository, times(1)).findAll(1);
    }

    @Test
    void shouldReturnCommentById() {
        String postId = "1";
        String commentId = "2";
        Comment comment = new Comment(2, 1, "text2");

        when(commentDaoRepository.findCommentById(1, 2)).thenReturn(comment);

        Comment result = commentService.findCommentById(postId, commentId);

        assertEquals(comment, result);
        verify(commentDaoRepository, times(1)).findCommentById(1, 2);
    }

    @Test
    void shouldSaveComment() {
        String postId = "1";
        CommentCreateDto dto = new CommentCreateDto("text1", 1);
        Comment entity = new Comment(null, 1, "text1");
        Comment saved = new Comment(1, 1, "text1");

        when(commentDaoRepository.save(any(Integer.class), any(Comment.class))).thenReturn(saved);

        Comment result = commentService.save(postId, dto);

        assertEquals(saved, result);

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentDaoRepository).save(eq(1), captor.capture());
        assertEquals(entity.getText(), captor.getValue().getText());
        assertEquals(entity.getPostId(), captor.getValue().getPostId());
    }

    @Test
    void shouldUpdateComment() {
        String postId = "1";
        CommentUpdateDto dto = new CommentUpdateDto(2, "updated text", 1);
        Comment updated = new Comment(2, 1, "updated text");

        when(commentDaoRepository.findCommentById(1, updated.getId())).thenReturn(updated);

        Comment result = commentService.update(postId, dto);

        assertEquals(updated, result);

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentDaoRepository).update(eq(1), captor.capture());
        assertEquals(updated.getText(), captor.getValue().getText());
        assertEquals(updated.getPostId(), captor.getValue().getPostId());
    }

    @Test
    void shouldDeleteComment() {
        String postId = "1";
        String commentId = "2";

        commentService.delete(postId, commentId);

        verify(commentDaoRepository, times(1)).deleteById(1, 2);
    }

    @Test
    void shouldThrowExceptionIfDeleteIdIsInvalid() {
        String postId = "1";
        String commentId = "abc";

        assertThrows(NumberFormatException.class,
                () -> commentService.delete(postId, commentId));

        verify(commentDaoRepository, never()).deleteById(anyInt(), anyInt());
    }

}