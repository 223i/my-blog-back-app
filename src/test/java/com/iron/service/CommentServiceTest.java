package com.iron.service;

import com.iron.dto.comment.CommentCreateDto;
import com.iron.dto.comment.CommentResponseDto;
import com.iron.dto.comment.CommentUpdateDto;
import com.iron.mapper.CommentDtoMapper;
import com.iron.model.Comment;
import com.iron.repository.CommentDaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.ArgumentCaptor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class CommentServiceTest {

    @Autowired
    private CommentDtoMapper commentDtoMapper;

    @MockitoBean
    private CommentDaoRepository commentDaoRepository;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    void setup() {
        reset(commentDaoRepository);
    }

    @Test
    void shouldReturnAllCommentsForPost() {
        Integer postId = 1;
        List<Comment> comments = List.of(
                new Comment(1, 1, "text1"),
                new Comment(2, 1, "text2")
        );

        when(commentDaoRepository.findAll(postId)).thenReturn(comments);

        List<CommentResponseDto> result = commentService.findAll(postId);

        assertEquals(comments.size(), result.size());
        assertEquals("text1", result.get(0).getText());
        assertEquals("text2", result.get(1).getText());

        verify(commentDaoRepository, times(1)).findAll(postId);
    }

    @Test
    void shouldReturnCommentById() {
        Integer postId = 1;
        Integer commentId = 2;
        Comment comment = new Comment(2, 1, "text2");

        when(commentDaoRepository.findCommentById(postId, commentId)).thenReturn(comment);

        CommentResponseDto result = commentService.findCommentById(postId, commentId);

        assertNotNull(result);
        assertEquals(comment.getText(), result.getText());
        assertEquals(comment.getPostId(), result.getPostId());

        verify(commentDaoRepository, times(1)).findCommentById(postId, commentId);
    }

    @Test
    void shouldSaveComment() {
        Integer postId = 1;
        CommentCreateDto dto = new CommentCreateDto("text1", 1);
        Comment saved = new Comment(1, 1, "text1");

        when(commentDaoRepository.save(any(Integer.class), any(Comment.class))).thenReturn(saved);

        CommentResponseDto result = commentService.save(postId, dto);

        assertEquals(dto.getText(), result.getText());
        assertEquals(dto.getPostId(), result.getPostId());

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentDaoRepository).save(eq(postId), captor.capture());
        assertEquals(dto.getText(), captor.getValue().getText());
        assertEquals(dto.getPostId(), captor.getValue().getPostId());
    }

    @Test
    void shouldUpdateComment() {
        Integer postId = 1;
        Integer commentId = 2;
        CommentUpdateDto dto = new CommentUpdateDto(commentId, "updated text", postId);
        Comment updated = new Comment(commentId, postId, "updated text");

        when(commentDaoRepository.findCommentById(postId, commentId)).thenReturn(updated);

        CommentResponseDto result = commentService.update(postId, commentId, dto);

        assertEquals(dto.getText(), result.getText());
        assertEquals(dto.getPostId(), result.getPostId());

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentDaoRepository).update(eq(postId), captor.capture());
        assertEquals(dto.getText(), captor.getValue().getText());
        assertEquals(dto.getPostId(), captor.getValue().getPostId());
    }

    @Test
    void shouldDeleteComment() {
        Integer postId = 1;
        Integer commentId = 2;

        commentService.delete(postId, commentId);

        verify(commentDaoRepository, times(1)).deleteById(postId, commentId);
    }
}
