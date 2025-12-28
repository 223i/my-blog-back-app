package com.iron.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iron.dto.comment.CommentCreateDto;
import com.iron.dto.comment.CommentUpdateDto;
import com.iron.model.Comment;
import com.iron.repository.CommentDaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CommentsControllerIntegrationTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    CommentDaoRepository commentDaoRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
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
        Comment existingComment = commentDaoRepository.findAll(1).getFirst();

        mockMvc.perform(get("/api/posts/1/comments/{id}", existingComment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingComment.getId()))
                .andExpect(jsonPath("$.text").value(existingComment.getText()));
    }

    @Test
    void shouldCreateCommentForPost1() throws Exception {
        CommentCreateDto dto = new CommentCreateDto();
        dto.setText("Новый комментарий для теста");
        dto.setPostId(1);

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
        Comment existingComment = commentDaoRepository.findAll(1).getFirst();

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
        assertEquals("Обновленный комментарий", updated.getText());
    }

    @Test
    void shouldDeleteComment() throws Exception {
        Comment existingComment = commentDaoRepository.findAll(1).getFirst();

        mockMvc.perform(delete("/api/posts/1/comments/{id}", existingComment.getId()))
                .andExpect(status().isOk());

        assertFalse(commentDaoRepository.findAll(1)
                .stream().anyMatch(c -> c.getId().equals(existingComment.getId())));
    }

}

