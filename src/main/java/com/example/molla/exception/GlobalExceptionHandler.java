package com.example.molla.exception;

import com.example.molla.common.StandardResponse;
import com.example.molla.domain.chatroom.exception.WebSocketException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    /**
     * 유효하지 않은 입력 값으로 요청 보낼 때
     * 주로 정수 값으로 파싱하지 못하는 값을 보낼 때 발생
     * ex) userId = "ffff"인 경우 파싱 불가
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardResponse<ErrorResponse>> handleMethodArgumentTypeMismatchException(final HttpMessageNotReadableException e) {
        log.error("MethodArgumentTypeMismatchException : ", e);

        final ErrorResponse errorResponse = ErrorResponse.of(e, ErrorCode.INVALID_INPUT_TYPE);
        return StandardResponse.of(errorResponse);
    }

    /**
     * WebSocket 관련 에러 처리
     */
    @ExceptionHandler(WebSocketException.class)
    public ResponseEntity<StandardResponse<ErrorResponse>> handleWebSocketException(final WebSocketException e) {
        log.error("WebSocketException : ", e);

        final ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode());
        return StandardResponse.of(errorResponse);
    }
}
