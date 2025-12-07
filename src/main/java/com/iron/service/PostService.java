package com.iron.service;

import com.iron.dto.PostCreateDto;
import com.iron.dto.PostUpdateDto;
import com.iron.mapper.PostDtoMapper;
import com.iron.model.Post;
import com.iron.repository.PostDaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostDaoRepository postDaoRepository;
    private final PostDtoMapper postDtoMapper;

    public PostService(PostDaoRepository postDaoRepository, PostDtoMapper postDtoMapper) {
        this.postDaoRepository = postDaoRepository;
        this.postDtoMapper = postDtoMapper;
    }

    public List<Post> findAll(){
        return postDaoRepository.findAll();
    }

    public Post findPostById(String id){
        return postDaoRepository.findPostById(Integer.valueOf(id));
    }

    public Post save (PostCreateDto post){
        return postDaoRepository.save(postDtoMapper.postCreateDtoToEntity(post));
    }

    public Post update(PostUpdateDto post){
        return postDaoRepository.update(postDtoMapper.postUpdateDtoToEntity(post));
    }

    public void delete(String id){
         postDaoRepository.deleteById(Integer.valueOf(id));
    }
}
