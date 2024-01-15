package com.example.jelog.web.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnlikePostRequestDto {
    private String token;
    private Long postId;
    private String userEmail;
}
