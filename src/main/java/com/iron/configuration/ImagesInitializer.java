package com.iron.configuration;

import com.iron.repository.ImagesDaoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;

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
                    System.out.println("Загружено изображение для postId=" + postId);
                } catch (Exception e) {
                    System.err.println("Ошибка при загрузке файла для postId=" + postId);
                    e.printStackTrace();
                }
            } else {
                System.err.println("Файл для postId=" + postId + " не найден");
            }
        }
    }
}