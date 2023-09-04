package com.example.jelog.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateArticleRequestDto {
    private String title;
    private String content;
    private String tags;
}
