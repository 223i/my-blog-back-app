package com.iron.controller;

import com.iron.service.LikesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/posts")
public class LikesController {

    private final LikesService likesService;

    public LikesController(LikesService likesService) {
        this.likesService = likesService;
    }

    @PostMapping("{id}/likes")
    public ResponseEntity<Integer> incrementLikes(@PathVariable("id") String postId){
        Integer amountOfLikes = likesService.update(postId);
        return ResponseEntity.ok().body(amountOfLikes);
    }
}
