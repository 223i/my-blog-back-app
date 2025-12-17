package com.iron.service;

import com.iron.dto.post.PostCreateDto;
import com.iron.dto.post.PostUpdateDto;
import com.iron.dto.post.PostsPageDto;
import com.iron.mapper.PostDtoMapper;
import com.iron.model.Post;
import com.iron.repository.PostDaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostDaoRepository postDaoRepository;
    private final PostDtoMapper postDtoMapper;

    public PostService(PostDaoRepository postDaoRepository, PostDtoMapper postDtoMapper) {
        this.postDaoRepository = postDaoRepository;
        this.postDtoMapper = postDtoMapper;
    }

    public PostsPageDto findAll(String searchText, Integer pageNumber, Integer pageSize){
        long totalElements = postDaoRepository.countPosts(searchText);

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        List<Post> postsForPage = postDaoRepository.findPostsForPage(searchText, pageNumber, pageSize);
        PostsPageDto response = new PostsPageDto();
        response.setPosts(postsForPage);
        response.setHasPrev(pageNumber > 1);
        response.setHasNext(pageNumber < totalPages);
        response.setLastPage(totalPages);
        return response;
    }

    public Post findPostById(Integer id){
        return postDaoRepository.findPostById(id);
    }

    public Post save (PostCreateDto post){
        return postDaoRepository.save(postDtoMapper.postCreateDtoToEntity(post));
    }

    public Post update(Integer id, PostUpdateDto post){
        Post existingPost = postDaoRepository.findPostById(id);
        Post updatedPost = postDtoMapper.postUpdateDtoToEntity(post);
        updatedPost.setId(id);

        if (existingPost != null) {
            // Сохраняем текущие значения счетчиков
            updatedPost.setLikesCount(Optional.ofNullable(existingPost.getLikesCount()).orElse(0));
            updatedPost.setCommentsCount(Optional.ofNullable(existingPost.getCommentsCount()).orElse(0));
        } else {
            // Если поста нет, создаём новый с нуля
            updatedPost.setLikesCount(0);
            updatedPost.setCommentsCount(0);
        }

        postDaoRepository.update(updatedPost);
        return postDaoRepository.findPostById(id);
    }

    public void delete(Integer id){
         postDaoRepository.deleteById(id);
    }
}
