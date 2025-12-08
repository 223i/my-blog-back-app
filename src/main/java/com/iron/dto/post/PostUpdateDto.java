package com.iron.dto.post;

import com.iron.util.validator.RequiredField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateDto {

    @RequiredField(message = "Id is required")
    private Integer id;

    @RequiredField(message = "Title is required")
    private String title;

    @RequiredField(message = "Text is required")
    private String text;

    @RequiredField(message = "Tags are required")
    private List<String> tags;
}
