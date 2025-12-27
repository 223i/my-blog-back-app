package com.iron.mapper;

import com.iron.dto.comment.CommentCreateDto;
import com.iron.dto.comment.CommentResponseDto;
import com.iron.dto.comment.CommentUpdateDto;
import com.iron.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentDtoMapper {

    public Comment commentCreateDtoToEntity(CommentCreateDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setPostId(dto.getPostId());
        return comment;
    }

    public Comment commentUpdateDtoToEntity(CommentUpdateDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setPostId(dto.getPostId());
        comment.setId(dto.getId());
        return comment;
    }

    public CommentResponseDto commentEntityToCommentResponseDto(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(comment.getId());
        commentResponseDto.setPostId(comment.getPostId());
        commentResponseDto.setText(comment.getText());
        return commentResponseDto;
    }
}
