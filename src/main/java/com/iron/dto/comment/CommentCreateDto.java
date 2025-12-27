package com.iron.dto.comment;

import com.iron.util.validator.RequiredField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateDto {

    @RequiredField(message = "Text is required")
    private String text;
    @RequiredField(message = "PostId is required")
    private Integer postId;
}
