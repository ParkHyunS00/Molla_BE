package com.example.molla.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private String status;
    private String errorCode;
    private String message;

    private ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getHttpStatus().toString();
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }
}
