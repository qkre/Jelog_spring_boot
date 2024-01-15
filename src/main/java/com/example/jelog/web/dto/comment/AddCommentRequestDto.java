package com.example.jelog.web.dto.comment;

import lombok.Getter;

@Getter
public class AddCommentRequestDto {
    private String token;
    private String userEmail;
    private Long postId;
    private String content;
}
