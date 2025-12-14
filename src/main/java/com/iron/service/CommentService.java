package com.iron.service;

import com.iron.dto.comment.CommentCreateDto;
import com.iron.dto.comment.CommentUpdateDto;
import com.iron.mapper.CommentDtoMapper;
import com.iron.model.Comment;
import com.iron.model.Post;
import com.iron.repository.CommentDaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentDaoRepository commentDaoRepository;
    private final CommentDtoMapper commentDtoMapper;

    public CommentService(CommentDaoRepository commentDaoRepository, CommentDtoMapper commentDtoMapper) {
        this.commentDaoRepository = commentDaoRepository;
        this.commentDtoMapper = commentDtoMapper;
    }
    
    public List<Comment> findAll(String post_id){
        return commentDaoRepository.findAll(Integer.valueOf(post_id));
    }

    public Comment findCommentById(String post_id, String id){
        return commentDaoRepository.findCommentById(Integer.valueOf(post_id), Integer.valueOf(id));
    }

    public Comment save (String post_id, CommentCreateDto comment){
        return commentDaoRepository.save(Integer.valueOf(post_id),
                commentDtoMapper.commentCreateDtoToEntity(comment));
    }

    public Comment update(String post_id, String commentId, CommentUpdateDto comment){
         commentDaoRepository.update(Integer.valueOf(post_id),
                commentDtoMapper.commentUpdateDtoToEntity(comment));
         return commentDaoRepository.findCommentById(Integer.valueOf(post_id), Integer.valueOf(commentId));
    }

    public void delete(String post_id, String id){
        commentDaoRepository.deleteById(Integer.valueOf(post_id), Integer.valueOf(id));
    }
}
