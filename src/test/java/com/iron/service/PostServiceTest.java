package com.iron.service;

import com.iron.dto.post.PostCreateDto;
import com.iron.dto.post.PostResponseDto;
import com.iron.dto.post.PostUpdateDto;
import com.iron.dto.post.PostsPageDto;
import com.iron.model.Post;
import com.iron.model.PostSearchCriteria;
import com.iron.repository.PostDaoRepository;
import com.iron.config.UnitTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UnitTestConfig.class)
public class PostServiceTest {

    @Autowired
    private PostDaoRepository postDaoRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void resetMocks() {
        reset(postDaoRepository);
    }

    @Test
    public void checkPostIsCreatedSuccessCase() {
        PostCreateDto postCreateDto = new PostCreateDto("post title", "post text",
                List.of("tag1", "tag2"), 5, 1);

        Post savedPost = new Post(1, postCreateDto.getTitle(), postCreateDto.getText(),
                postCreateDto.getTags(), postCreateDto.getLikesCount(), postCreateDto.getCommentsCount());

        when(postDaoRepository.save(any(Post.class))).thenReturn(savedPost);

        PostResponseDto result = postService.save(postCreateDto);

        assertNotNull(result);
        assertEquals(savedPost.getId(), result.getId());
        assertEquals(postCreateDto.getTitle(), result.getTitle());
        assertEquals(postCreateDto.getText(), result.getText());
        assertEquals(postCreateDto.getTags(), result.getTags());
        assertEquals(postCreateDto.getLikesCount(), result.getLikesCount());
        assertEquals(postCreateDto.getCommentsCount(), result.getCommentsCount());

        verify(postDaoRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void checkPostIsUpdatedSuccessCase() {
        PostUpdateDto postUpdateDto = new PostUpdateDto(1, "updated title", "updated text", List.of("tag1"));

        Post existingPost = new Post(1, "old title", "old text", List.of("oldTag"), 5, 2);
        when(postDaoRepository.findPostById(1)).thenReturn(existingPost);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);

        PostResponseDto result = postService.update(1, postUpdateDto);

        verify(postDaoRepository).update(captor.capture());
        Post postToUpdate = captor.getValue();

        assertNotNull(result);
        assertEquals(existingPost.getId(), result.getId());
        assertEquals(postUpdateDto.getTitle(), postToUpdate.getTitle());
        assertEquals(postUpdateDto.getText(), postToUpdate.getText());
        assertEquals(postUpdateDto.getTags(), postToUpdate.getTags());
        assertEquals(existingPost.getLikesCount(), postToUpdate.getLikesCount());
        assertEquals(existingPost.getCommentsCount(), postToUpdate.getCommentsCount());
    }

    @Test
    void shouldDeletePost() {
        postService.delete(1);
        verify(postDaoRepository, times(1)).deleteById(1);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingPost() {
        doThrow(new EmptyResultDataAccessException(1)).when(postDaoRepository).deleteById(999);
        assertThrows(EmptyResultDataAccessException.class, () -> postService.delete(999));
    }

    @Test
    void shouldReturnPostById() {
        Post post = new Post(1, "title", "text", List.of("tag1"), 0, 0);
        when(postDaoRepository.findPostById(1)).thenReturn(post);

        PostResponseDto response = postService.findPostById(1);

        assertNotNull(response);
        assertEquals(post.getId(), response.getId());
        assertEquals(post.getTitle(), response.getTitle());
        assertEquals(post.getText(), response.getText());
        assertEquals(post.getTags(), response.getTags());
        verify(postDaoRepository).findPostById(1);
    }

    @Test
    void shouldThrowExceptionIfPostNotFoundById() {
        when(postDaoRepository.findPostById(999)).thenThrow(new EmptyResultDataAccessException(1));
        assertThrows(EmptyResultDataAccessException.class, () -> postService.findPostById(999));
    }

    @Test
    void shouldReturnPostsForTitleSearch() {
        int pageNumber = 1, pageSize = 2;

        Post post1 = new Post(1, "Spring Boot Guide", "Text1", List.of("java"), 5, 0);
        Post post2 = new Post(2, "Spring Data JPA", "Text2", List.of("java", "db"), 3, 1);
        List<Post> posts = List.of(post1, post2);

        when(postDaoRepository.countPosts(any(PostSearchCriteria.class))).thenReturn(2L);
        when(postDaoRepository.findPostsForPage(any(PostSearchCriteria.class), eq(pageNumber), eq(pageSize)))
                .thenReturn(posts);

        PostsPageDto result = postService.findAll("Spring", pageNumber, pageSize);

        assertEquals(2, result.getPosts().size());
        assertTrue(result.getPosts().stream().allMatch(p -> p.getTitle().contains("Spring")));

        ArgumentCaptor<PostSearchCriteria> criteriaCaptor = ArgumentCaptor.forClass(PostSearchCriteria.class);
        verify(postDaoRepository).countPosts(criteriaCaptor.capture());
        verify(postDaoRepository).findPostsForPage(criteriaCaptor.capture(), eq(pageNumber), eq(pageSize));

        PostSearchCriteria usedCriteria = criteriaCaptor.getAllValues().get(0);
        assertEquals("Spring", usedCriteria.getTitleSubstring());
        assertTrue(usedCriteria.getTags().isEmpty());
    }

    @Test
    void shouldReturnPostsForTagSearch() {
        int pageNumber = 1, pageSize = 2;

        Post post = new Post(1, "Post 1", "Text1", List.of("java", "spring"), 0, 0);
        when(postDaoRepository.countPosts(any(PostSearchCriteria.class))).thenReturn(1L);
        when(postDaoRepository.findPostsForPage(any(PostSearchCriteria.class), eq(pageNumber), eq(pageSize)))
                .thenReturn(List.of(post));

        PostsPageDto result = postService.findAll("#java #spring", pageNumber, pageSize);

        assertEquals(1, result.getPosts().size());
        assertTrue(result.getPosts().get(0).getTags().containsAll(List.of("java", "spring")));

        ArgumentCaptor<PostSearchCriteria> criteriaCaptor = ArgumentCaptor.forClass(PostSearchCriteria.class);
        verify(postDaoRepository).countPosts(criteriaCaptor.capture());
        verify(postDaoRepository).findPostsForPage(criteriaCaptor.capture(), eq(pageNumber), eq(pageSize));

        PostSearchCriteria usedCriteria = criteriaCaptor.getAllValues().get(0);
        assertEquals("", usedCriteria.getTitleSubstring());
        assertEquals(List.of("java", "spring"), usedCriteria.getTags());
    }

    @Test
    void shouldReturnPostsForCombinedSearch() {
        int pageNumber = 1, pageSize = 1;

        Post post = new Post(1, "Spring Boot Guide", "Content", List.of("java"), 5, 0);
        when(postDaoRepository.countPosts(any(PostSearchCriteria.class))).thenReturn(1L);
        when(postDaoRepository.findPostsForPage(any(PostSearchCriteria.class), eq(pageNumber), eq(pageSize)))
                .thenReturn(List.of(post));

        PostsPageDto result = postService.findAll("#java Spring Boot", pageNumber, pageSize);

        assertEquals(1, result.getPosts().size());
        Post resultPost = result.getPosts().get(0);
        assertEquals("Spring Boot Guide", resultPost.getTitle());
        assertTrue(resultPost.getTags().contains("java"));

        ArgumentCaptor<PostSearchCriteria> criteriaCaptor = ArgumentCaptor.forClass(PostSearchCriteria.class);
        verify(postDaoRepository).countPosts(criteriaCaptor.capture());
        verify(postDaoRepository).findPostsForPage(criteriaCaptor.capture(), eq(pageNumber), eq(pageSize));

        PostSearchCriteria usedCriteria = criteriaCaptor.getAllValues().get(0);
        assertEquals("Spring Boot", usedCriteria.getTitleSubstring());
        assertEquals(List.of("java"), usedCriteria.getTags());
    }

    @Test
    void shouldReturnEmptyWhenNoPostsMatchCriteria() {
        int pageNumber = 1, pageSize = 5;

        when(postDaoRepository.countPosts(any(PostSearchCriteria.class))).thenReturn(0L);
        when(postDaoRepository.findPostsForPage(any(PostSearchCriteria.class), eq(pageNumber), eq(pageSize)))
                .thenReturn(Collections.emptyList());

        PostsPageDto result = postService.findAll("#tagX NonExisting", pageNumber, pageSize);

        assertTrue(result.getPosts().isEmpty());
        assertFalse(result.isHasPrev());
        assertFalse(result.isHasNext());
        assertEquals(0, result.getLastPage());
    }
}
