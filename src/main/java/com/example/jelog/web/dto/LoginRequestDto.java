package com.example.jelog.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String userEmail;
    private String userPw;
}
