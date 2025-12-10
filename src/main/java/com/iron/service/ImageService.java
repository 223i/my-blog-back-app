package com.iron.service;

import com.iron.repository.PostDaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final PostDaoRepository postRepository;

    public ImageService(PostDaoRepository postRepository) {
        this.postRepository = postRepository;
    }


    public byte[] getImage(String post_id) {
        return postRepository.getImage(Integer.valueOf(post_id));
    }

    public void uploadImage(String post_id, MultipartFile file) {
        postRepository.findPostById(Integer.valueOf(post_id)); //проверяем что пост с таким айди существует
        postRepository.saveImage(Integer.valueOf(post_id), file);
    }

}
