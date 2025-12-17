package com.iron.service;

import com.iron.model.Post;
import com.iron.repository.PostDaoRepository;
import org.springframework.stereotype.Service;

@Service
public class LikesService {

    private final PostDaoRepository postRepository;

    public LikesService(PostDaoRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Integer update(Integer postId) {
        Post post = postRepository.findPostById(postId);
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.update(post);
        return post.getLikesCount();
    }
}
