package com.example.molla.exception;

import com.example.molla.common.StandardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직 수행하며 발생하는 예외 처리 담당
     * ex) 중복된 이메일, 이미 존재하는 유저 등
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardResponse<ErrorResponse>> handleBusinessException(final BusinessException e) {
        log.error("BusinessException : ", e);

        final ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode());
        return StandardResponse.of(errorResponse);
    }

    /**
     * 유효하지 않은 입력 값으로 요청 보낼 때
     * 주로 @Valid 어노테이션으로 입력값 유효성 검증시 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse<ErrorResponse>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : ", e);

        final ErrorResponse errorResponse = ErrorResponse.of(e, ErrorCode.INVALID_INPUT_VALUE);
        return StandardResponse.of(errorResponse);
    }
}
