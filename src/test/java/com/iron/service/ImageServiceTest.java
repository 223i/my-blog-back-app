package com.iron.service;

import com.iron.model.Post;
import com.iron.repository.ImagesDaoRepository;
import com.iron.repository.PostDaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class ImageServiceTest {

    @MockitoBean
    private ImagesDaoRepository imagesDaoRepository;

    @MockitoBean
    private PostDaoRepository postDaoRepository;

    @Autowired
    private ImageService imageService;


    @BeforeEach
    void resetMocks() {
        reset(imagesDaoRepository);
        reset(postDaoRepository);
    }


    @Test
    void shouldReturnImageBytes() {
        byte[] imageBytes = new byte[]{1, 2, 3};

        when(imagesDaoRepository.getImage(1)).thenReturn(imageBytes);

        byte[] result = imageService.getImage(1);

        assertArrayEquals(imageBytes, result);
        verify(imagesDaoRepository, times(1)).getImage(1);
    }

    @Test
    void shouldUploadImageSuccessfully() throws Exception {

        MultipartFile file = mock(MultipartFile.class);
        when(postDaoRepository.findPostById(1)).thenReturn(new Post());

        imageService.uploadImage(1, file);

        verify(postDaoRepository, times(1)).findPostById(1);
        verify(imagesDaoRepository, times(1)).saveImage(1, file);
    }

    @Test
    void shouldThrowExceptionIfPostNotFound() {

        MultipartFile file = mock(MultipartFile.class);

        when(postDaoRepository.findPostById(1)).thenThrow(new NoSuchElementException());

        assertThrows(NoSuchElementException.class, () -> imageService.uploadImage(1, file));

        verify(postDaoRepository, times(1)).findPostById(1);
        verify(imagesDaoRepository, never()).saveImage(anyInt(), (MultipartFile) any());
    }
}
