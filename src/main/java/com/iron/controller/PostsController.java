package com.iron.controller;

import com.iron.dto.post.PostCreateDto;
import com.iron.dto.post.PostUpdateDto;
import com.iron.dto.post.PostsPageDto;
import com.iron.model.Post;
import com.iron.service.PostService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/posts")
public class PostsController {

    private final PostService postService;

    public PostsController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping
    public PostsPageDto getAllPosts(@RequestParam(value = "search", defaultValue = "") String searchText,
                                          @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return postService.findAll(searchText, pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable("id") Integer id) {
        return postService.findPostById(id);
    }

    @PostMapping
    public Post createPost(@Valid @RequestBody PostCreateDto post) {
        return postService.save(post);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable("id") Integer id, @Valid @RequestBody PostUpdateDto post) {
        return postService.update(id, post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Integer id) {
        postService.delete(id);
    }
}
