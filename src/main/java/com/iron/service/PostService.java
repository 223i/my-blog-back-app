package com.iron.service;

import com.iron.model.Post;
import com.iron.repository.PostDaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostDaoRepository postDaoRepository;

    public PostService(PostDaoRepository postDaoRepository) {
        this.postDaoRepository = postDaoRepository;
    }

    public List<Post> findAll(){
        return postDaoRepository.findAll();
    }

    public Post findPostById(String id){
        return postDaoRepository.findPostById(Integer.valueOf(id));
    }

    public Post save (Post post){
        return postDaoRepository.save(post);
    }
}
