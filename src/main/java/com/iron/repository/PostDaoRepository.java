package com.iron.repository;

import com.iron.model.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostDaoRepository {

    List<Post> findPostsForPage(String postTitle, int pageNumber, int pageSize);

    Long countPosts(String postTitle);

    Post findPostById(Integer id);

    Post save(Post post);

    void update(Post post);

    void deleteById(Integer id);
}
