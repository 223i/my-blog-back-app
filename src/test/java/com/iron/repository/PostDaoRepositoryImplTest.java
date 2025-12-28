package com.iron.repository;

import com.iron.model.Post;
import com.iron.model.PostSearchCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(PostDaoRepositoryImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PostDaoRepositoryImplTest {

    @Autowired
    private PostDaoRepositoryImpl repository;

    @Test
    void shouldFindPostByIdWithTags() {
        Post post = repository.findPostById(1);

        assertEquals("Тестирование Spring Boot", post.getTitle());
        assertEquals(3, post.getTags().size());
        assertTrue(post.getTags().contains("java"));
        assertTrue(post.getTags().contains("spring"));
        assertTrue(post.getTags().contains("testing"));
    }

    @Test
    void shouldThrowExceptionIfPostNotFound() {
        assertThrows(
                NoSuchElementException.class,
                () -> repository.findPostById(999)
        );
    }

    @Test
    void shouldFindPostsForPageWithoutFilters() {
        PostSearchCriteria criteria = new PostSearchCriteria();

        List<Post> posts = repository.findPostsForPage(criteria, 1, 10);

        assertEquals(2, posts.size());
        posts.forEach(p -> assertNotNull(p.getTags()));
    }

    @Test
    void shouldFilterPostsByTitle() {
        PostSearchCriteria criteria = new PostSearchCriteria();
        criteria.setTitleSubstring("Spring");

        List<Post> posts = repository.findPostsForPage(criteria, 1, 10);

        assertEquals(1, posts.size());
        assertEquals("Тестирование Spring Boot", posts.getFirst().getTitle());
    }

    @Test
    void shouldFilterPostsByTags() {
        PostSearchCriteria criteria = new PostSearchCriteria();
        criteria.setTags(List.of("spring"));

        List<Post> posts = repository.findPostsForPage(criteria, 1, 10);

        assertEquals(1, posts.size());
        assertEquals("Тестирование Spring Boot", posts.getFirst().getTitle());
    }

    @Test
    void shouldCountPostsByCriteria() {
        PostSearchCriteria criteria = new PostSearchCriteria();
        criteria.setTags(List.of("spring"));

        Long count = repository.countPosts(criteria);

        assertEquals(1L, count);
    }

    @Test
    void shouldSavePostWithTags() {
        Post post = new Post(
                null,
                "New post",
                "New text",
                List.of("java", "testing"),
                0,
                0
        );

        Post saved = repository.save(post);

        assertNotNull(saved.getId());

        Post fromDb = repository.findPostById(saved.getId());
        assertEquals("New post", fromDb.getTitle());
        assertEquals(2, fromDb.getTags().size());
    }

    @Test
    void shouldUpdatePostAndTags() {
        Post post = repository.findPostById(1);
        post.setTitle("Updated title");
        post.setTags(List.of("updated", "java"));

        repository.update(post);

        Post updated = repository.findPostById(1);
        assertEquals("Updated title", updated.getTitle());
        assertEquals(2, updated.getTags().size());
        assertTrue(updated.getTags().contains("updated"));
    }

    @Test
    void shouldDeletePostWithTags() {
        repository.deleteById(1);

        assertThrows(
                NoSuchElementException.class,
                () -> repository.findPostById(1)
        );
    }
}
