package com.iron.repository;

import org.springframework.web.multipart.MultipartFile;

public interface ImagesDaoRepository {
    void saveImage(Integer postId, MultipartFile file);
    void saveImage(Integer postId, byte[] file);
    byte[] getImage(Integer postId);
}
