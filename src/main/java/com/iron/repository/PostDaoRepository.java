package com.iron.repository;

import com.iron.model.Post;
import com.iron.model.PostSearchCriteria;

import java.util.List;

public interface PostDaoRepository {

    List<Post> findPostsForPage(PostSearchCriteria postTitle, int pageNumber, int pageSize);

    Long countPosts(PostSearchCriteria postTitle);

    Post findPostById(Integer id);

    Post save(Post post);

    void update(Post post);

    void deleteById(Integer id);
}
