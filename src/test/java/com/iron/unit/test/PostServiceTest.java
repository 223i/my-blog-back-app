package com.iron.unit.test;

import com.iron.dto.post.PostCreateDto;
import com.iron.dto.post.PostUpdateDto;
import com.iron.dto.post.PostsPageDto;
import com.iron.model.Post;
import com.iron.repository.PostDaoRepository;
import com.iron.service.PostService;
import com.iron.unit.test.configuration.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
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
        Post validPost = new Post(1, "post title", "post text", List.of("tag1", "tag2"), 5, 1);
        PostCreateDto postCreateDto = new PostCreateDto("post title", "post text",
                List.of("tag1", "tag2"), 5, 1);
        when(postDaoRepository.save(any(Post.class))).thenReturn(validPost);

        Post result = postService.save(postCreateDto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(postCreateDto.getTitle(), result.getTitle());
        assertEquals(postCreateDto.getText(), result.getText());
        assertEquals(postCreateDto.getTags(), result.getTags());
        assertEquals(postCreateDto.getLikesCount(), result.getLikesCount());
        assertEquals(postCreateDto.getCommentsCount(), result.getCommentsCount());
        verify(postDaoRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void checkPostIsUpdatedSuccessCase() {
        // given
        Post validPost = new Post(1, "post title", "post text", List.of("tag1", "tag2"), 5, 1);
        PostUpdateDto postUpdateDto = new PostUpdateDto(1, "post title", "post text",
                List.of("tag1", "tag2"));
        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        when(postDaoRepository.findPostById(any(Integer.class))).thenReturn(validPost);

        //when
        Post result = postService.update("1", postUpdateDto);

        //then
        verify(postDaoRepository, times(1)).update(captor.capture());
        Post postToUpdate = captor.getValue();

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(postToUpdate.getTitle(), result.getTitle());
        assertEquals(postToUpdate.getText(), result.getText());
        assertEquals(postToUpdate.getTags(), result.getTags());
        assertEquals(postToUpdate.getLikesCount(), result.getLikesCount());
        assertEquals(postToUpdate.getCommentsCount(), result.getCommentsCount());
    }

    @Test
    public void checkCreatePostEntityBuildCorrectly() {
        // given
        PostCreateDto postCreateDto = new PostCreateDto("post title", "post text",
                List.of("tag1", "tag2"), 5, 1);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);

        // when
        postService.save(postCreateDto);

        // then
        verify(postDaoRepository).save(captor.capture());
        Post postToSave = captor.getValue();

        assertEquals(postCreateDto.getTitle(), postToSave.getTitle());
        assertEquals(postCreateDto.getText(), postToSave.getText());
        assertEquals(postCreateDto.getTags(), postToSave.getTags());
        assertEquals(postCreateDto.getLikesCount(), postToSave.getLikesCount());
        assertEquals(postCreateDto.getCommentsCount(), postToSave.getCommentsCount());
        verify(postDaoRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void checkUpdatePostEntityBuildCorrectly() {
        // given
        PostUpdateDto postUpdateDto = new PostUpdateDto(1, "post title", "post text",
                List.of("tag1", "tag2"));
        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);

        // when
        postService.update("1", postUpdateDto);

        // then
        verify(postDaoRepository).update(captor.capture());
        Post postToUpdate = captor.getValue();

        assertEquals(postUpdateDto.getTitle(), postToUpdate.getTitle());
        assertEquals(postUpdateDto.getText(), postToUpdate.getText());
        assertEquals(postUpdateDto.getTags(), postToUpdate.getTags());
    }

    @Test
    public void checkDeletePostCalledOneTime() {
        //when
        postService.delete("1");

        //then
        verify(postDaoRepository, times(1)).deleteById(any(Integer.class));
    }

    @Test
    public void checkDeletePostParameterParsedCorrectly() {
        // given
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        //when
        postService.delete("1");

        //then
        verify(postDaoRepository).deleteById(captor.capture());
        assertEquals(1, captor.getValue());
    }

    @Test
    public void checkDeleteIfWrongParameterTypePassed() {
        assertThrows(NumberFormatException.class,
                () -> postService.delete("abc"));
        verify(postDaoRepository, never()).deleteById(any());
    }

    @Test
    public void checkDeletePostShouldPropagateException() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(postDaoRepository).deleteById(1);

        assertThrows(EmptyResultDataAccessException.class,
                () -> postService.delete("1"));
    }


    @Test
    public void checkGetPostByIdSuccessCase(){
        // given
        Post validPost = new Post(1, "post title", "post text", List.of("tag1", "tag2"), 5, 1);
        when(postDaoRepository.findPostById(any(Integer.class))).thenReturn(validPost);

        // when
        Post result = postService.findPostById("1");

        // then
        verify(postDaoRepository, times(1)).findPostById(any(Integer.class));
        assertNotNull(result);
        assertEquals(validPost.getTitle(), result.getTitle());
        assertEquals(validPost.getText(), result.getText());
        assertEquals(validPost.getTags(), result.getTags());
        assertEquals(validPost.getLikesCount(), result.getLikesCount());
        assertEquals(validPost.getCommentsCount(), result.getCommentsCount());
    }

    @Test
    public void checkNoSuchElementExceptionIfNoPostsFoundInDb(){
        doThrow(new EmptyResultDataAccessException(1))
                .when(postDaoRepository).findPostById(1);

        assertThrows(EmptyResultDataAccessException.class,
                () -> postService.findPostById("1"));
    }

    @Test
    public void checkFindPostByIdIfWrongParameterTypePassed() {
        assertThrows(NumberFormatException.class,
                () -> postService.findPostById("abc"));
        verify(postDaoRepository, never()).findPostById(any());
    }


    @Test
    void shouldReturnPostsPage_whenResultsExist() {
        // given
        String searchText = "test";
        int pageNumber = 0;
        int pageSize = 2;

        Post post1 = new Post(1, "title1", "text1", List.of("tag1"), 5, 2);
        Post post2 = new Post(2, "title2", "text2", List.of("tag2"), 3, 1);
        List<Post> posts = List.of(post1, post2);

        when(postDaoRepository.countPosts(searchText)).thenReturn(5L);
        when(postDaoRepository.findPostsForPage(searchText, pageNumber, pageSize)).thenReturn(posts);

        // when
        PostsPageDto result = postService.findAll(searchText, pageNumber, pageSize);

        // then
        assertNotNull(result);
        assertEquals(posts, result.getPosts());
        assertFalse(result.isHasPrev()); // pageNumber == 0
        assertTrue(result.isHasNext());  // totalPages = 3, pageNumber < 2
        assertEquals(3, result.getLastPage());

        verify(postDaoRepository, times(1)).countPosts(searchText);
        verify(postDaoRepository, times(1)).findPostsForPage(searchText, pageNumber, pageSize);
    }

    @Test
    void shouldReturnEmptyPostsPage_whenNoResults() {
        // given
        String searchText = "empty";
        int pageNumber = 0;
        int pageSize = 5;

        when(postDaoRepository.countPosts(searchText)).thenReturn(0L);
        when(postDaoRepository.findPostsForPage(searchText, pageNumber, pageSize)).thenReturn(List.of());

        // when
        PostsPageDto result = postService.findAll(searchText, pageNumber, pageSize);

        // then
        assertNotNull(result);
        assertTrue(result.getPosts().isEmpty());
        assertFalse(result.isHasPrev());
        assertFalse(result.isHasNext());
        assertEquals(0, result.getLastPage());

        verify(postDaoRepository).countPosts(searchText);
        verify(postDaoRepository).findPostsForPage(searchText, pageNumber, pageSize);
    }

    @Test
    void shouldSetCorrectHasPrevAndHasNextFlags() {
        // given
        String searchText = "";
        int pageNumber = 2;
        int pageSize = 2;

        List<Post> posts = List.of(
                new Post(3, "title3", "text3", List.of(), 0, 0)
        );

        when(postDaoRepository.countPosts(searchText)).thenReturn(5L); // totalPages = 3
        when(postDaoRepository.findPostsForPage(searchText, pageNumber, pageSize)).thenReturn(posts);

        // when
        PostsPageDto result = postService.findAll(searchText, pageNumber, pageSize);

        // then
        assertTrue(result.isHasPrev());  // pageNumber > 0
        assertFalse(result.isHasNext()); // last page
        assertEquals(3, result.getLastPage());
    }

    @Test
    void shouldWorkWithPageNumberGreaterThanZero() {
        // given
        String searchText = "anything";
        int pageNumber = 1;
        int pageSize = 2;

        List<Post> posts = List.of(
                new Post(2, "title2", "text2", List.of(), 0, 0)
        );

        when(postDaoRepository.countPosts(searchText)).thenReturn(3L);
        when(postDaoRepository.findPostsForPage(searchText, pageNumber, pageSize)).thenReturn(posts);

        // when
        PostsPageDto result = postService.findAll(searchText, pageNumber, pageSize);

        // then
        assertTrue(result.isHasPrev());
        assertFalse(result.isHasNext()); // pageNumber == lastPage-1
        assertEquals(2, result.getLastPage());
    }

    @Test
    void shouldReturnAllPostsWhenSearchTextIsEmpty() {
        // given
        String searchText = "";
        int pageNumber = 0;
        int pageSize = 3;

        List<Post> posts = List.of(
                new Post(1, "title1", "text1", List.of("tag1"), 0, 0),
                new Post(2, "title2", "text2", List.of("tag2"), 0, 0),
                new Post(3, "title3", "text3", List.of("tag3"), 0, 0)
        );

        when(postDaoRepository.countPosts(searchText)).thenReturn(5L);
        when(postDaoRepository.findPostsForPage(searchText, pageNumber, pageSize)).thenReturn(posts);

        // when
        PostsPageDto result = postService.findAll(searchText, pageNumber, pageSize);

        // then
        assertEquals(posts, result.getPosts());
        assertFalse(result.isHasPrev());
        assertTrue(result.isHasNext());  // totalPages = 2, pageNumber = 0
        assertEquals(2, result.getLastPage());

        verify(postDaoRepository).countPosts(searchText);
        verify(postDaoRepository).findPostsForPage(searchText, pageNumber, pageSize);
    }

    @Test
    void shouldReturnAllPostsWhenSearchTextIsNull() {
        // given
        String searchText = null;
        int pageNumber = 0;
        int pageSize = 2;

        List<Post> posts = List.of(
                new Post(1, "title1", "text1", List.of(), 0, 0),
                new Post(2, "title2", "text2", List.of(), 0, 0)
        );

        when(postDaoRepository.countPosts(searchText)).thenReturn(4L);
        when(postDaoRepository.findPostsForPage(searchText, pageNumber, pageSize)).thenReturn(posts);

        // when
        PostsPageDto result = postService.findAll(searchText, pageNumber, pageSize);

        // then
        assertEquals(posts, result.getPosts());
        assertFalse(result.isHasPrev());
        assertTrue(result.isHasNext());  // totalPages = 2, pageNumber = 0
        assertEquals(2, result.getLastPage());

        verify(postDaoRepository).countPosts(searchText);
        verify(postDaoRepository).findPostsForPage(searchText, pageNumber, pageSize);
    }


}
