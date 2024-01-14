package com.example.jelog.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnlikePostRequestDto {
    private Long postId;
    private Long userId;
}
