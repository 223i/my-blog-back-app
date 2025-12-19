package com.iron.service;

import com.iron.config.UnitTestConfig;
import com.iron.dto.comment.CommentCreateDto;
import com.iron.dto.comment.CommentResponseDto;
import com.iron.dto.comment.CommentUpdateDto;
import com.iron.mapper.CommentDtoMapper;
import com.iron.model.Comment;
import com.iron.repository.CommentDaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UnitTestConfig.class)
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
        Integer postId = 1;
        List<Comment> comments = List.of(
                new Comment(1, 1, "text1"),
                new Comment(2, 1, "text2")
        );

        when(commentDaoRepository.findAll(postId)).thenReturn(comments);

        Map<Integer, Comment> commentById = comments.stream()
                .collect(Collectors.toMap(Comment::getId, Function.identity()));

        List<CommentResponseDto> result = commentService.findAll(postId);

        assertEquals(comments.size(), result.size());
        result.forEach(dto -> {
            Comment comment = commentById.get(dto.getId());
            assertNotNull(comment);

            assertEquals(comment.getPostId(), dto.getPostId());
            assertEquals(comment.getText(), dto.getText());
        });


        verify(commentDaoRepository, times(1)).findAll(1);
    }

    @Test
    void shouldReturnCommentById() {
        Integer postId = 1;
        Integer commentId = 2;
        Comment comment = new Comment(2, 1, "text2");

        when(commentDaoRepository.findCommentById(1, 2)).thenReturn(comment);

        CommentResponseDto result = commentService.findCommentById(postId, commentId);

        assertNotNull(result);
        assertEquals(result.getPostId(), comment.getPostId());
        assertEquals(result.getText(), comment.getText());

        verify(commentDaoRepository, times(1)).findCommentById(1, 2);
    }

    @Test
    void shouldSaveComment() {
        Integer postId = 1;
        CommentCreateDto dto = new CommentCreateDto("text1", 1);
        Comment entity = new Comment(null, 1, "text1");
        Comment saved = new Comment(1, 1, "text1");

        when(commentDaoRepository.save(any(Integer.class), any(Comment.class))).thenReturn(saved);

        CommentResponseDto result = commentService.save(postId, dto);

        assertNotNull(result);
        assertEquals(result.getPostId(), dto.getPostId());
        assertEquals(result.getText(), dto.getText());

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentDaoRepository).save(eq(1), captor.capture());
        assertEquals(entity.getText(), captor.getValue().getText());
        assertEquals(entity.getPostId(), captor.getValue().getPostId());
    }

    @Test
    void shouldUpdateComment() {
        Integer postId = 1;
        Integer commentId = 2;
        CommentUpdateDto dto = new CommentUpdateDto(2, "updated text", 1);
        Comment updated = new Comment(2, 1, "updated text");

        when(commentDaoRepository.findCommentById(1, updated.getId())).thenReturn(updated);

        CommentResponseDto result = commentService.update(postId, commentId,  dto);

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentDaoRepository).update(eq(1), captor.capture());
        assertEquals(result.getText(), captor.getValue().getText());
        assertEquals(result.getPostId(), captor.getValue().getPostId());
    }

    @Test
    void shouldDeleteComment() {
        Integer postId = 1;
        Integer commentId = 2;

        commentService.delete(postId, commentId);

        verify(commentDaoRepository, times(1)).deleteById(1, 2);
    }
}