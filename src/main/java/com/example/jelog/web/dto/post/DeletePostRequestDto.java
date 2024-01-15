package com.example.jelog.web.dto.post;

import lombok.Getter;

@Getter
public class DeletePostRequestDto {
    private String userEmail;
    private Long postId;
    private String token;
}
