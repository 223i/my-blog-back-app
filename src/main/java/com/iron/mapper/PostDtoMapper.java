package com.iron.mapper;

import com.iron.dto.post.PostCreateDto;
import com.iron.dto.post.PostResponseDto;
import com.iron.dto.post.PostUpdateDto;
import com.iron.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostDtoMapper {

    public Post postCreateDtoToEntity(PostCreateDto dto) {
        Post post =  new Post();
        post.setText(dto.getText());
        post.setTitle(dto.getTitle());
        post.setTags(dto.getTags());
        post.setLikesCount(dto.getLikesCount());
        post.setCommentsCount(dto.getCommentsCount());
        return post;
    }

    public Post postUpdateDtoToEntity(PostUpdateDto dto) {
        Post post =  new Post();
        post.setId(dto.getId());
        post.setText(dto.getText());
        post.setTitle(dto.getTitle());
        post.setTags(dto.getTags());
        return post;
    }

    public PostResponseDto postEntityToPostResponseDto(Post post){
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setId(post.getId());
        postResponseDto.setText(post.getText());
        postResponseDto.setTags(post.getTags());
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setCommentsCount(post.getCommentsCount());
        postResponseDto.setLikesCount(post.getLikesCount());
        return postResponseDto;
    }
}
