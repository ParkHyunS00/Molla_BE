package com.example.molla.common;

import lombok.Getter;

@Getter
public class DeleteResponse {

    private final String message;
    private final Long id;

    public DeleteResponse(Long id, String type) {
        this.message = "한 개의 " + type + " 삭제 완료";
        this.id = id;
    }
}
