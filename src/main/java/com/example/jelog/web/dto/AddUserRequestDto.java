package com.example.jelog.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequestDto {

    private String userEmail;
    private String userPw;
    private String userIcon;
}
