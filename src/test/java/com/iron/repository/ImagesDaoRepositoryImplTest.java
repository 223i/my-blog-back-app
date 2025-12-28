package com.iron.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(ImagesDaoRepositoryImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ImagesDaoRepositoryImplTest {

    @Autowired
    private ImagesDaoRepositoryImpl repository;

    @Test
    void shouldSaveAndLoadImage() {
        byte[] image = new byte[]{1, 2, 3, 4};

        repository.saveImage(1, image);

        byte[] loaded = repository.getImage(1);

        assertNotNull(loaded);
        assertArrayEquals(image, loaded);
    }

    @Test
    void shouldThrowExceptionIfImageNotFound() {
        assertThrows(
                DataRetrievalFailureException.class,
                () -> repository.getImage(999)
        );
    }

    @Test
    void shouldUpdateExistingImage() {
        byte[] newImage = new byte[]{9, 9, 9, 9};

        repository.saveImage(1, newImage);

        byte[] saved = repository.getImage(1);
        assertArrayEquals(newImage, saved);
    }

    @Test
    void shouldSaveImageFromMultipartFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "image.png",
                "image/png",
                new byte[]{5, 6, 7}
        );

        repository.saveImage(2, file);

        byte[] saved = repository.getImage(2);
        assertArrayEquals(new byte[]{5, 6, 7}, saved);
    }
}
