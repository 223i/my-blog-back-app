package com.iron.service;

import com.iron.model.Post;
import com.iron.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public Post findPostById(String id){
        return postRepository.findPostById(Integer.valueOf(id));
    }
}
