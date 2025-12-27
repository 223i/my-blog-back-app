package com.iron.service;

import com.iron.model.Post;
import com.iron.repository.PostDaoRepository;
import com.iron.config.UnitTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UnitTestConfig.class)
public class LikesServiceTest {

    @Autowired
    private PostDaoRepository postRepository;

    @Autowired
    private LikesService likesService;


    @BeforeEach
    void resetMocks() {
        reset(postRepository);
    }

    @Test
    void shouldIncreaseLikesCountAndReturnNewValue() {
        // given
        Integer postId = 1;
        Post existingPost = new Post();
        existingPost.setId(1);
        existingPost.setLikesCount(5);

        when(postRepository.findPostById(1)).thenReturn(existingPost);

        // when
        Integer result = likesService.update(postId);

        // then
        assertEquals(6, result);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).update(captor.capture());

        Post updatedPost = captor.getValue();
        assertEquals(6, updatedPost.getLikesCount());

        verify(postRepository, times(1)).findPostById(1);
        verify(postRepository, times(1)).update(any(Post.class));
    }

    @Test
    void shouldIncreaseLikesFromZero() {
        // given
        Post post = new Post();
        post.setId(1);
        post.setLikesCount(0);

        when(postRepository.findPostById(1)).thenReturn(post);

        // when
        Integer result = likesService.update(1);

        // then
        assertEquals(1, result);
        verify(postRepository).update(post);
    }

    @Test
    void shouldThrowExceptionWhenPostNotFound() {
        // given
        when(postRepository.findPostById(1)).thenReturn(null);

        // then
        assertThrows(NullPointerException.class,
                () -> likesService.update(1));

        verify(postRepository, times(1)).findPostById(1);
        verify(postRepository, never()).update(any());
    }
}
