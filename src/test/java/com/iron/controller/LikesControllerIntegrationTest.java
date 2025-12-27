package com.iron.controller;

import com.iron.config.IntegrationTestConfig;
import com.iron.model.Post;
import com.iron.repository.PostDaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = IntegrationTestConfig.class)
class LikesControllerIntegrationTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    PostDaoRepository postDaoRepository;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

    @Test
    void shouldIncrementLikesForPost1() throws Exception {
        // Берем пост с id=1 из базы
        Post before = postDaoRepository.findPostById(1);
        int initialLikes = before.getLikesCount();

        mockMvc.perform(post("/api/posts/1/likes"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(initialLikes + 1)));

        Post after = postDaoRepository.findPostById(1);
        assertTrue(after.getLikesCount().equals(initialLikes + 1));
    }

    @Test
    void shouldIncrementLikesForPost2() throws Exception {
        // Берем пост с id=2 из базы
        Post before = postDaoRepository.findPostById(2);
        int initialLikes = before.getLikesCount();

        mockMvc.perform(post("/api/posts/2/likes"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(initialLikes + 1)));

        Post after = postDaoRepository.findPostById(2);
        assertTrue(after.getLikesCount().equals(initialLikes + 1));
    }
}