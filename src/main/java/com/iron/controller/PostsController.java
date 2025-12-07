package com.iron.controller;

import com.iron.dto.PostCreateDto;
import com.iron.dto.PostUpdateDto;
import com.iron.model.Post;
import com.iron.service.PostService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostsController {

    private final PostService postService;

    public PostsController(PostService postService) {
        this.postService = postService;
    }

    //TODO: Добавить обработку пагинации
    @GetMapping
    public List<Post> getAllPosts(@RequestParam(value = "search", defaultValue = "") String searchText,
                                  @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable("id") String id) {
        return postService.findPostById(id);
    }

    @PostMapping
    public Post createPost(@Valid @RequestBody PostCreateDto post) {
        return postService.save(post);
    }

    @PutMapping
    public Post updatePost(@Valid @RequestBody PostUpdateDto post) {
        return postService.update(post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") String id) {
        postService.delete(id);
    }
}
