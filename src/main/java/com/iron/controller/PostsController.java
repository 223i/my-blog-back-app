package com.iron.controller;

import com.iron.model.Post;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostsController {

    @GetMapping
    @ResponseBody
    public List<Post> getAllPosts() {
        Post post1 = new Post();
        post1.setId(1);
        post1.setTitle("Post 1");
        post1.setLikesCount(5);
        post1.setTags(List.of("tag1", "tag2"));
        post1.setCommentsCount(1);

        Post post2 = new Post();
        post2.setId(1);
        post2.setTitle("Post 1");
        post2.setLikesCount(5);
        post2.setTags(List.of("tag1", "tag2"));
        post2.setCommentsCount(1);
        return List.of(post1, post2);
    }
}
