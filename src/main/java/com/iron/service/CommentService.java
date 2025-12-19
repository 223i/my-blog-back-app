package com.iron.service;

import com.iron.dto.comment.CommentCreateDto;
import com.iron.dto.comment.CommentResponseDto;
import com.iron.dto.comment.CommentUpdateDto;
import com.iron.mapper.CommentDtoMapper;
import com.iron.model.Comment;
import com.iron.repository.CommentDaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentDaoRepository commentDaoRepository;
    private final CommentDtoMapper commentDtoMapper;

    public CommentService(CommentDaoRepository commentDaoRepository, CommentDtoMapper commentDtoMapper) {
        this.commentDaoRepository = commentDaoRepository;
        this.commentDtoMapper = commentDtoMapper;
    }

    public List<CommentResponseDto> findAll(Integer post_id) {
        List<Comment> comments = commentDaoRepository.findAll(post_id);
        return comments.stream()
                .map(commentDtoMapper::commentEntityToCommentResponseDto)
                .toList();
    }

    public CommentResponseDto findCommentById(Integer post_id, Integer id) {
        Comment comment = commentDaoRepository.findCommentById(post_id, id);
        return commentDtoMapper.commentEntityToCommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto save(Integer post_id, CommentCreateDto comment) {
        Comment savedComment = commentDaoRepository.save(post_id,
                commentDtoMapper.commentCreateDtoToEntity(comment));
        return commentDtoMapper.commentEntityToCommentResponseDto(savedComment);
    }

    @Transactional
    public CommentResponseDto update(Integer post_id, Integer commentId, CommentUpdateDto comment) {
        commentDaoRepository.update(post_id, commentDtoMapper.commentUpdateDtoToEntity(comment));
        Comment updatedComment = commentDaoRepository.findCommentById(post_id, commentId);
        return commentDtoMapper.commentEntityToCommentResponseDto(updatedComment);
    }

    @Transactional
    public void delete(Integer post_id, Integer id) {
        commentDaoRepository.deleteById(post_id, id);
    }
}
