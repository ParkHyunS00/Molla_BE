package com.example.molla.common;

import lombok.Getter;

@Getter
public class UpdateResponse {

    private final String message;
    private final Long id;

    public UpdateResponse(Long id, String type) {
        this.message = "한 개의 " + type + " 수정 완료";
        this.id = id;
    }
}
