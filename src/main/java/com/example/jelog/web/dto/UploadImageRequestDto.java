package com.example.jelog.web.dto;

import com.example.jelog.domain.post.Post;
import com.example.jelog.domain.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadImageRequestDto {
    private Post post;
    private User user;
    private String fileName;
}
