package com.iron.controller;

import com.iron.service.ImageService;
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
    public ResponseEntity<byte[]> getPostImage(@PathVariable String post_id) {
        byte[] image = imageService.getImage(post_id);

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateImage(@PathVariable("post_id") String post_id,
                                            @RequestParam("image") MultipartFile file) {
        imageService.uploadImage(post_id, file);
        return ResponseEntity.ok().build();
    }
}
