package com.iron.repository;

import com.iron.model.Post;

import java.util.List;

public interface PostDaoRepository {

     List<Post> findAll();
     Post findPostById(Integer id);
     Post save(Post post);
}
