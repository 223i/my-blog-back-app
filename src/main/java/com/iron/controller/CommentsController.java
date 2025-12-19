package com.iron.controller;

import com.iron.dto.comment.CommentCreateDto;
import com.iron.dto.comment.CommentResponseDto;
import com.iron.dto.comment.CommentUpdateDto;
import com.iron.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{post_id}/comments")
public class CommentsController {

    private final CommentService commentService;

    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentResponseDto> getAllComments(@PathVariable("post_id") Integer post_id) {
        return commentService.findAll(post_id);
    }

    @GetMapping("/{id}")
    public CommentResponseDto getCommentById(@PathVariable("post_id") Integer post_id, @PathVariable("id") Integer id) {
        return commentService.findCommentById(post_id, id);
    }

    @PostMapping
    public CommentResponseDto createComment(@PathVariable("post_id") Integer post_id, @Valid @RequestBody CommentCreateDto comment) {
        return commentService.save(post_id, comment);
    }

    @PutMapping("/{id}")
    public CommentResponseDto updateComment(@PathVariable("post_id") Integer post_id, @PathVariable("id") Integer commentId,
                                            @Valid @RequestBody CommentUpdateDto comment) {
        return commentService.update(post_id, commentId, comment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable("post_id") Integer post_id, @PathVariable("id") Integer id) {
        commentService.delete(post_id, id);
    }
}
