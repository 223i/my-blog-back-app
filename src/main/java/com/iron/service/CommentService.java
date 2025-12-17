package com.iron.service;

import com.iron.dto.comment.CommentCreateDto;
import com.iron.dto.comment.CommentUpdateDto;
import com.iron.mapper.CommentDtoMapper;
import com.iron.model.Comment;
import com.iron.repository.CommentDaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentDaoRepository commentDaoRepository;
    private final CommentDtoMapper commentDtoMapper;

    public CommentService(CommentDaoRepository commentDaoRepository, CommentDtoMapper commentDtoMapper) {
        this.commentDaoRepository = commentDaoRepository;
        this.commentDtoMapper = commentDtoMapper;
    }

    public List<Comment> findAll(Integer post_id) {
        return commentDaoRepository.findAll(post_id);
    }

    public Comment findCommentById(Integer post_id, Integer id) {
        return commentDaoRepository.findCommentById(post_id, id);
    }

    public Comment save(Integer post_id, CommentCreateDto comment) {
        return commentDaoRepository.save(post_id,
                commentDtoMapper.commentCreateDtoToEntity(comment));
    }

    public Comment update(Integer post_id, Integer commentId, CommentUpdateDto comment) {
        commentDaoRepository.update(post_id,
                commentDtoMapper.commentUpdateDtoToEntity(comment));
        return commentDaoRepository.findCommentById(post_id, commentId);
    }

    public void delete(Integer post_id, Integer id) {
        commentDaoRepository.deleteById(post_id, id);
    }
}
