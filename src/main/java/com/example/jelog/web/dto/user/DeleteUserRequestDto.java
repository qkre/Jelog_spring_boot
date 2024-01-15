package com.example.jelog.web.dto.user;

import lombok.Getter;

@Getter
public class DeleteUserRequestDto {
    private String token;
    private String userEmail;
}
