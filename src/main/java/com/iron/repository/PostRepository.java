package com.iron.repository;

import com.iron.model.Post;

import java.util.List;

public interface PostRepository {

     List<Post> findAll();
     Post findPostById(Integer id);
}
