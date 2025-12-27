package com.iron.service;

import com.iron.repository.ImagesDaoRepository;
import com.iron.repository.PostDaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final ImagesDaoRepository imagesRepository;
    private final PostDaoRepository postDaoRepository;

    public ImageService(ImagesDaoRepository imagesRepository, PostDaoRepository postDaoRepository) {
        this.imagesRepository = imagesRepository;
        this.postDaoRepository = postDaoRepository;
    }


    public byte[] getImage(Integer post_id) {
        return imagesRepository.getImage(post_id);
    }

    @Transactional
    public void uploadImage(Integer post_id, MultipartFile file) {
        postDaoRepository.findPostById(post_id); //проверяем что пост с таким айди существует
        imagesRepository.saveImage(post_id, file);
    }

}
