package com.example.jelog.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPostRequestDto {
    private String userEmail;
    private String title;
    private String content;
}
