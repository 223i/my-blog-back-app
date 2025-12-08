package com.iron.repository;

import com.iron.model.Comment;

import java.util.List;

public interface CommentDaoRepository {

    List<Comment> findAll(Integer post_id);

    Comment findCommentById(Integer post_id, Integer id);

    Comment save(Integer post_id, Comment comment);

    Comment update(Integer post_id, Comment comment);

    void deleteById(Integer post_id, Integer id);
}
