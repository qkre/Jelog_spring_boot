package com.example.jelog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_EMAIL_DUPLICATED(HttpStatus.CONFLICT, "중복된 이메일"),
    USER_DONT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 이메일"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호"),
    POSTS_DONT_EXIST(HttpStatus.NOT_FOUND, "포스트가 없습니다."),
    WRONG_ACCEPT(HttpStatus.BAD_REQUEST, "잘못된 접근");

    private HttpStatus httpStatus;
    private String message;
}
