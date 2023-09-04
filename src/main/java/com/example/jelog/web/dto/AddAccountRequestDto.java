package com.example.jelog.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddAccountRequestDto {

    private String email;
    private String password;
    private String userID;
    private String icon;
}
