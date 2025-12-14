package com.iron.unit.test.configuration;

import com.iron.mapper.CommentDtoMapper;
import com.iron.mapper.PostDtoMapper;
import com.iron.repository.CommentDaoRepository;
import com.iron.repository.ImagesDaoRepository;
import com.iron.repository.PostDaoRepository;
import com.iron.service.CommentService;
import com.iron.service.ImageService;
import com.iron.service.LikesService;
import com.iron.service.PostService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public PostDaoRepository mockPostDaoRepository() {
        return Mockito.mock(PostDaoRepository.class);
    }

    @Bean
    public PostDtoMapper postDtoMapper() {
        return new PostDtoMapper();
    }

    @Bean
    public PostService postService(PostDaoRepository repository, PostDtoMapper postDtoMapper) {
        return new PostService(repository, postDtoMapper);
    }

    @Bean
    public CommentDaoRepository mockCommentDaoRepository() {
        return Mockito.mock(CommentDaoRepository.class);
    }

    @Bean
    public CommentDtoMapper commentDtoMapper() {
        return new CommentDtoMapper();
    }

    @Bean
    public CommentService commentService(CommentDaoRepository repository, CommentDtoMapper commentDtoMapper) {
        return new CommentService(repository, commentDtoMapper);
    }

    @Bean
    public ImagesDaoRepository imagesDaoRepository(){
        return Mockito.mock(ImagesDaoRepository.class);
    }

    @Bean
    public ImageService imageService(ImagesDaoRepository imagesDaoRepository, PostDaoRepository postDaoRepository){
        return new ImageService(imagesDaoRepository, postDaoRepository);
    }

    @Bean
    public LikesService likesService(PostDaoRepository postDaoRepository){
        return new LikesService(postDaoRepository);
    }
}
