package com.example.jelog.web.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddPostRequestDto {
    private String token;
    private String userEmail;
    private String title;
    private String content;
    private List<String> tags;
}
