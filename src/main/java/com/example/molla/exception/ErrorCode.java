package com.example.molla.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // User
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, "U01", "이미 존재하는 회원입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U02", "사용자를 찾을 수 없습니다."),
    LOGIN_INFO_NOT_MATCHED(HttpStatus.BAD_REQUEST, "U03", "로그인 정보가 일치하지 않습니다."),

    // Post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P01", "해당 ID로 된 게시글을 찾을 수 없습니다."),

    // Diary
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "D01", "해당 ID로 된 일기를 찾을 수 없습니다."),

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C01", "유효하지 않은 입력 값 입니다.")
    ;

    private final String message;
    private final String code;
    private final HttpStatus httpStatus;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
