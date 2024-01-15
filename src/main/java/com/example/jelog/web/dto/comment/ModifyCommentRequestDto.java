package com.example.jelog.web.dto.comment;

import lombok.Getter;

@Getter
public class ModifyCommentRequestDto {
    private String token;
    private Long commentId;
    private String userEmail;
    private Long postId;
    private String content;
}
