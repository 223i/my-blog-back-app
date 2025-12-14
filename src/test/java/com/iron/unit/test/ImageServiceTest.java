package com.iron.unit.test;

import com.iron.model.Post;
import com.iron.repository.ImagesDaoRepository;
import com.iron.repository.PostDaoRepository;
import com.iron.service.ImageService;
import com.iron.unit.test.configuration.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class ImageServiceTest {

    @Autowired
    private ImagesDaoRepository imagesDaoRepository;

    @Autowired
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
        String postId = "1";
        byte[] imageBytes = new byte[]{1, 2, 3};

        when(imagesDaoRepository.getImage(1)).thenReturn(imageBytes);

        byte[] result = imageService.getImage(postId);

        assertArrayEquals(imageBytes, result);
        verify(imagesDaoRepository, times(1)).getImage(1);
    }

    @Test
    void shouldUploadImageSuccessfully() throws Exception {
        String postId = "1";
        MultipartFile file = mock(MultipartFile.class);
        when(postDaoRepository.findPostById(1)).thenReturn(new Post());

        imageService.uploadImage(postId, file);

        verify(postDaoRepository, times(1)).findPostById(1);
        verify(imagesDaoRepository, times(1)).saveImage(1, file);
    }

    @Test
    void shouldThrowExceptionIfPostNotFound()  {
        String postId = "1";
        MultipartFile file = mock(MultipartFile.class);

        when(postDaoRepository.findPostById(1)).thenThrow(new NoSuchElementException());

        assertThrows(NoSuchElementException.class, () -> imageService.uploadImage(postId, file));

        verify(postDaoRepository, times(1)).findPostById(1);
        verify(imagesDaoRepository, never()).saveImage(anyInt(), (MultipartFile) any());
    }
}
