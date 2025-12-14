package com.iron.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iron.config.IntegrationTestConfig;
import com.iron.dto.comment.CommentCreateDto;
import com.iron.dto.comment.CommentUpdateDto;
import com.iron.model.Comment;
import com.iron.repository.CommentDaoRepository;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = IntegrationTestConfig.class)
class CommentsControllerIntegrationTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    CommentDaoRepository commentDaoRepository;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

    @Test
    void shouldReturnAllCommentsForPost1() throws Exception {
        mockMvc.perform(get("/api/posts/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(commentDaoRepository.findAll(1).size()));
    }

    @Test
    void shouldReturnSingleComment() throws Exception {
        Comment existingComment = commentDaoRepository.findAll(1).get(0);

        mockMvc.perform(get("/api/posts/1/comments/{id}", existingComment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingComment.getId()))
                .andExpect(jsonPath("$.text").value(existingComment.getText()));
    }

    @Test
    void shouldCreateCommentForPost1() throws Exception {
        CommentCreateDto dto = new CommentCreateDto();
        dto.setText("Новый комментарий для теста");

        mockMvc.perform(post("/api/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Новый комментарий для теста"));

        List<Comment> comments = commentDaoRepository.findAll(1);
        assertTrue(comments.stream().anyMatch(c -> "Новый комментарий для теста".equals(c.getText())));
    }

    @Test
    void shouldUpdateCommentForPost1() throws Exception {
        Comment existingComment = commentDaoRepository.findAll(1).get(0);

        CommentUpdateDto dto = new CommentUpdateDto();
        dto.setId(existingComment.getId());
        dto.setText("Обновленный комментарий");
        dto.setPostId(existingComment.getPostId());

        mockMvc.perform(put("/api/posts/1/comments/{id}", existingComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Обновленный комментарий"));

        Comment updated = commentDaoRepository.findCommentById(1, existingComment.getId());
        assertTrue(updated.getText().equals("Обновленный комментарий"));
    }

    @Test
    void shouldDeleteComment() throws Exception {
        Comment existingComment = commentDaoRepository.findAll(1).get(0);

        mockMvc.perform(delete("/api/posts/1/comments/{id}", existingComment.getId()))
                .andExpect(status().isOk());

        assertFalse(commentDaoRepository.findAll(1)
                .stream().anyMatch(c -> c.getId().equals(existingComment.getId())));
    }

}

