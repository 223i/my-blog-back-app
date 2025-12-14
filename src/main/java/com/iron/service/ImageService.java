package com.iron.service;

import com.iron.repository.ImagesDaoRepository;
import com.iron.repository.PostDaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final ImagesDaoRepository imagesRepository;
    private final PostDaoRepository postDaoRepository;

    public ImageService(ImagesDaoRepository imagesRepository, PostDaoRepository postDaoRepository) {
        this.imagesRepository = imagesRepository;
        this.postDaoRepository = postDaoRepository;
    }


    public byte[] getImage(String post_id) {
        return imagesRepository.getImage(Integer.valueOf(post_id));
    }

    public void uploadImage(String post_id, MultipartFile file) {
        postDaoRepository.findPostById(Integer.valueOf(post_id)); //проверяем что пост с таким айди существует
        imagesRepository.saveImage(Integer.valueOf(post_id), file);
    }

}
