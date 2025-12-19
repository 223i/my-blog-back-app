package com.iron.service;

import com.iron.dto.post.PostCreateDto;
import com.iron.dto.post.PostResponseDto;
import com.iron.dto.post.PostUpdateDto;
import com.iron.dto.post.PostsPageDto;
import com.iron.mapper.PostDtoMapper;
import com.iron.model.Post;
import com.iron.model.PostSearchCriteria;
import com.iron.repository.PostDaoRepository;
import com.iron.util.exceptionHandler.PostSearchParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostDaoRepository postDaoRepository;
    private final PostDtoMapper postDtoMapper;
    private final PostSearchParser postSearchParser;

    public PostService(PostDaoRepository postDaoRepository, PostDtoMapper postDtoMapper,
                       PostSearchParser postSearchParser) {
        this.postDaoRepository = postDaoRepository;
        this.postDtoMapper = postDtoMapper;
        this.postSearchParser = postSearchParser;
    }

    public PostsPageDto findAll(String searchText, Integer pageNumber, Integer pageSize){
        PostSearchCriteria searchCriteria = postSearchParser.parse(searchText);

        long totalElements = postDaoRepository.countPosts(searchCriteria);
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        List<Post> postsForPage = postDaoRepository.findPostsForPage(searchCriteria, pageNumber, pageSize);

        PostsPageDto response = new PostsPageDto();
        response.setPosts(postsForPage);
        response.setHasPrev(pageNumber > 1);
        response.setHasNext(pageNumber < totalPages);
        response.setLastPage(totalPages);
        return response;
    }

    public PostResponseDto findPostById(Integer id){
        Post post =  postDaoRepository.findPostById(id);
        return  postDtoMapper.postEntityToPostResponseDto(post);
    }

    @Transactional
    public PostResponseDto save (PostCreateDto post){
        Post savedPost =  postDaoRepository.save(postDtoMapper.postCreateDtoToEntity(post));
        return postDtoMapper.postEntityToPostResponseDto(savedPost);
    }

    @Transactional
    public PostResponseDto update(Integer id, PostUpdateDto post){
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
        Post updatedPost2 = postDaoRepository.findPostById(id);
        return postDtoMapper.postEntityToPostResponseDto(updatedPost2);
    }

    @Transactional
    public void delete(Integer id){
         postDaoRepository.deleteById(id);
    }
}
