package com.iron.configuration;

import com.iron.repository.ImagesDaoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;

@Component
public class ImagesInitializer {

    private final ImagesDaoRepository imagesDaoRepository;

    public ImagesInitializer(JdbcTemplate jdbcTemplate, ImagesDaoRepository imagesDaoRepository) {
        this.imagesDaoRepository = imagesDaoRepository;
    }

    @PostConstruct
    public void initImages() {
        for (int postId = 1; postId <= 11; postId++) {
            String[] extensions = {"png", "jpg", "jpeg"};
            ClassPathResource resource = null;
            for (String ext : extensions) {
                String fileName = String.format("images/post%d.%s", postId, ext);
                ClassPathResource candidate = new ClassPathResource(fileName);
                if (candidate.exists()) {
                    resource = candidate;
                    break;
                }
                try {
                    byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());

                    imagesDaoRepository.saveImage(postId, imageBytes);

                } catch (IOException e) {
                    System.err.println("Не удалось загрузить файл: " + fileName);
                    e.printStackTrace();
                }
            }
        }
    }
}
