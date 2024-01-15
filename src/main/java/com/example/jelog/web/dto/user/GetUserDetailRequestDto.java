package com.example.jelog.web.dto.user;

import lombok.Getter;

@Getter
public class GetUserDetailRequestDto {
    private String userEmail;
    private String token;
}
