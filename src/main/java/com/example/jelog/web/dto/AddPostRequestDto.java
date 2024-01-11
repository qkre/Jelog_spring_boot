package com.example.jelog.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddPostRequestDto {
    private String userEmail;
    private String title;
    private String content;
    private List<String> tags;
}
