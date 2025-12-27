package com.iron.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostSearchCriteria {
    private String titleSubstring;
    private List<String> tags;

    public boolean hasTitle() {
        return titleSubstring != null && !titleSubstring.isBlank();
    }

    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }
}
