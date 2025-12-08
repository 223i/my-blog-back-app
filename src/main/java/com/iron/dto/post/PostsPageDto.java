package com.iron.dto.post;

import com.iron.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostsPageDto {

    private List<Post> posts;
    private boolean hasPrev;
    private boolean hasNext;
    private int lastPage;
}
