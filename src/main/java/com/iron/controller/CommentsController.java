package com.iron.controller;

import com.iron.dto.comment.CommentCreateDto;
import com.iron.dto.comment.CommentUpdateDto;
import com.iron.model.Comment;
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
    public List<Comment> getAllComments(@PathVariable("post_id") String post_id) {
        return commentService.findAll(post_id);
    }

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable("post_id") String post_id, @PathVariable("id") String id) {
        return commentService.findCommentById(post_id, id);
    }

    @PostMapping
    public Comment createComment(@PathVariable("post_id") String post_id, @Valid @RequestBody CommentCreateDto comment) {
        return commentService.save(post_id, comment);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable("post_id") String post_id, @PathVariable("id") String commentId,
                                 @Valid @RequestBody CommentUpdateDto comment) {
        return commentService.update(post_id, commentId, comment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable("post_id") String post_id, @PathVariable("id") String id) {
        commentService.delete(post_id, id);
    }
}
