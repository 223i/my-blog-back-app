package com.iron.controller;

import com.iron.service.ImageService;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts/{post_id}/image")
public class ImagesController {

    private final ImageService imageService;

    public ImagesController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<byte[]> getPostImage(@PathVariable("post_id") Integer postId) {
        try {
            byte[] image = imageService.getImage(postId);
            if (image == null || image.length == 0) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(image);
            }
        } catch (DataRetrievalFailureException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateImage(@PathVariable("post_id") Integer postId,
                                            @RequestParam("image") MultipartFile file) {
        imageService.uploadImage(postId, file);
        return ResponseEntity.ok().build();
    }
}
