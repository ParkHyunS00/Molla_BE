package com.example.molla.exception;

import lombok.Getter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {

    private final int status;
    private final String errorCode;
    private final String message;
    private final Map<String, String> fieldErrors;

    private ErrorResponse(ErrorCode errorCode, String message, Map<String, String> fieldErrors) {
        this.status = errorCode.getHttpStatus().value();
        this.errorCode = errorCode.getCode();
        this.message = Optional.ofNullable(message).orElse(errorCode.getMessage());
        this.fieldErrors = Optional.ofNullable(fieldErrors).orElse(new HashMap<>());
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode, null, null);
    }

    // MethodArgumentNotValidException 예외 전용 메서드
    public static ErrorResponse of(MethodArgumentNotValidException e, ErrorCode errorCode) {
        Map<String, String> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("")));

        return new ErrorResponse(errorCode, errorCode.getMessage(), fieldErrors);
    }

    // HttpMessageNotReadableException 예외 전용 메서드
    public static ErrorResponse of(HttpMessageNotReadableException e, ErrorCode errorCode) {

        return new ErrorResponse(errorCode, errorCode.getMessage(), null);
    }
}
