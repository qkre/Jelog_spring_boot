package com.example.jelog.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikePostRequestDto {
    private Long postId;
    private Long userId;
}
