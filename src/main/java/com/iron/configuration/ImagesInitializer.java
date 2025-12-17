package com.iron.configuration;

import com.iron.repository.ImagesDaoRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;

@Slf4j
@Component
@Profile("!test")
public class ImagesInitializer {

    private final ImagesDaoRepository imagesDaoRepository;

    public ImagesInitializer(ImagesDaoRepository imagesDaoRepository) {
        this.imagesDaoRepository = imagesDaoRepository;
    }

    @PostConstruct
    public void initImages() {
        for (int postId = 1; postId <= 11; postId++) {
            String[] extensions = {"png", "jpg", "jpeg"};
            ClassPathResource resource = null;

            for (String ext : extensions) {
                String fileName = String.format("images/image%d.%s", postId, ext);
                ClassPathResource candidate = new ClassPathResource(fileName);
                if (candidate.exists()) {
                    resource = candidate;
                    break;
                }
            }

            if (resource != null) {
                try {
                    byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
                    imagesDaoRepository.saveImage(postId, imageBytes);
                    log.info("Загружено изображение для postId={}", postId);
                } catch (Exception e) {
                    log.error("Ошибка при загрузке файла для postId={}", postId, e);
                }
            } else {
                log.error("Файл для postId={} не найден", postId);
            }
        }
    }
}