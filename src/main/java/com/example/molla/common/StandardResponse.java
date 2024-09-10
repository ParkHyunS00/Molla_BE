package com.example.molla.common;

import com.example.molla.exception.ErrorResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

@Getter
@Builder
public class StandardResponse<T> {

    @NotNull
    private int status;

    @Nullable
    private T data;

    // 200 을 반환하는 일반적인 응답
    public static <T> ResponseEntity<StandardResponse<T>> ofOk(T data) {

        final StandardResponse<T> response =StandardResponse.<T>builder()
                .data(data)
                .status(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }

    // 200 을 반환하지 않는 응답 ex) 생성
    public static <T> ResponseEntity<StandardResponse<T>> of(T data, HttpStatus status){

        final StandardResponse<T> response = StandardResponse.<T>builder().data(data).status(status.value()).build();

        return new ResponseEntity<>(response, status);
    }

    /**
     * 잘못된 요청이나 서비스 로직 수행시 발생하는 예외 상황에 대한 응답
     * ex) 유효성 검증에 따른 예외 발생, 존재하지 않는 사용자 조회시
     */
    public static <T> ResponseEntity<StandardResponse<ErrorResponse>> of(ErrorResponse errorResponse) {

        final int status = errorResponse.getStatus();

        StandardResponse<ErrorResponse> response = StandardResponse.<ErrorResponse>builder()
                .data(errorResponse)
                .status(status)
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(status));
    }
}
