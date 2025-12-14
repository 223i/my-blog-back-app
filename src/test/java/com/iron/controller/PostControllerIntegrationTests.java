package com.iron.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iron.config.IntegrationTestConfig;
import com.iron.dto.post.PostCreateDto;
import com.iron.dto.post.PostUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = IntegrationTestConfig.class)
public class PostControllerIntegrationTests {

    @Autowired
    WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).alwaysDo(print()).build();
    }

    @Test
    void shouldReturnFirstPageOfPosts() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("pageNumber", "1")
                        .param("pageSize", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts").isArray())
                .andExpect(jsonPath("$.posts.length()").value(1))
                .andExpect(jsonPath("$.hasPrev").value(false))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.lastPage").value(2));
    }

    @Test
    void shouldReturnEmptyWhenNoPostsMatchSearch() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("search", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts").isArray())
                .andExpect(jsonPath("$.posts.length()").value(0))
                .andExpect(jsonPath("$.hasPrev").value(false))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.lastPage").value(0));
    }

    @Test
    void shouldReturnPostsWithSearchText() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("search", "Spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts").isArray())
                .andExpect(jsonPath("$.posts[0].title").value(containsString("Spring")));
    }

    @Test
    void shouldReturnEmptyPostsWhenSearchNotFound() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("search", "NonExistingPost"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts").isEmpty());
    }

    @Test
    void shouldReturnPostsWithCustomPagination() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("pageNumber", "2")
                        .param("pageSize", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastPage").value(2))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.hasPrev").value(true));
    }

    @Test
    void shouldReturnPostById() throws Exception {
        mockMvc.perform(get("/api/posts/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.text").exists());
    }

    @Test
    void shouldReturn404ForNonExistingPost() throws Exception {
        mockMvc.perform(get("/api/posts/{id}", 999))
                .andExpect(content().string(containsString("Post with id=999 not found")));
    }

    @Test
    void shouldCreatePost() throws Exception {
        PostCreateDto post = new PostCreateDto();
        post.setTitle("New Post");
        post.setText("Content of new post");
        post.setTags(List.of("tag1", "tag2"));

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("New Post"))
                .andExpect(jsonPath("$.text").value("Content of new post"));
    }

    @Test
    void shouldUpdatePost() throws Exception {
        PostUpdateDto update = new PostUpdateDto();
        update.setTitle("Updated Title");
        update.setText("Updated Text");

        mockMvc.perform(put("/api/posts/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.text").value("Updated Text"));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingPost() throws Exception {
        PostUpdateDto update = new PostUpdateDto();
        update.setTitle("Updated Title");
        update.setText("Updated Text");

        mockMvc.perform(put("/api/posts/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(content().string(containsString("Post with id=999 not found")));
    }


    @Test
    void shouldDeletePost() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}", 1))
                .andExpect(status().isOk());
    }
}
