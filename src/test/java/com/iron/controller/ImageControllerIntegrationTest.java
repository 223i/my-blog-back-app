package com.iron.controller;

import com.iron.config.IntegrationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = IntegrationTestConfig.class)
class ImageControllerIntegrationTest {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).alwaysDo(print()).build();
    }

    @Test
    void shouldUpdateImage() throws Exception {
        // Загружаем тестовую картинку из ресурсов
        Path imagePath = Paths.get("src/test/resources/images/image1.jpeg");
        byte[] imageBytes = Files.readAllBytes(imagePath);

        MockMultipartFile multipartFile = new MockMultipartFile(
                "image",                  // имя параметра в @RequestParam
                "image1.jpg",               // оригинальное имя файла
                MediaType.IMAGE_JPEG_VALUE,
                imageBytes
        );

        mockMvc.perform(multipart("/api/posts/{post_id}/image", 1)
                        .file(multipartFile)
                        .with(request -> {       // обязательно для PUT с multipart
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnExistingImage() throws Exception {
        mockMvc.perform(get("/api/posts/{post_id}/image", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(result -> {
                    byte[] body = result.getResponse().getContentAsByteArray();
                    assertTrue(body.length > 0, "Image should not be empty");
                });
        ;
    }

    @Test
    void shouldReturn404ForNonExistingImage() throws Exception {
        // GET для post_id = 2 (картинки нет)
        mockMvc.perform(get("/api/posts/{post_id}/image", 2))
                .andExpect(status().isNotFound());
    }
}