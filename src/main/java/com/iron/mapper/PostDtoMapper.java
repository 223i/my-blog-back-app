package com.iron.mapper;

import com.iron.dto.PostCreateDto;
import com.iron.dto.PostUpdateDto;
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
}
