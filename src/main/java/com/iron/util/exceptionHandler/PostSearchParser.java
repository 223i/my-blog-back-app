package com.iron.util.exceptionHandler;

import com.iron.model.PostSearchCriteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostSearchParser {

    public PostSearchCriteria parse(String searchText){
        if (searchText == null || searchText.isBlank()){
            return new PostSearchCriteria();
        }

        List<String> tags = new ArrayList<>();
        List<String> titleWords = new ArrayList<>();

        for (String word : searchText.trim().split("\\s+")) {
            if (word.isBlank()) continue;

            if (word.startsWith("#") && word.length() > 1) {
                tags.add(word.substring(1));
            } else {
                titleWords.add(word);
            }
        }

        PostSearchCriteria postSearchCriteria = new PostSearchCriteria();
        postSearchCriteria.setTags(tags);
        postSearchCriteria.setTitleSubstring(String.join(" ", titleWords));
        return postSearchCriteria;
    }
}
