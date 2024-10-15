package com.example.molla.domain.chatroom.dto;

import com.example.molla.domain.common.Status;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseMessageDTO {

    private Long userId; // ML 메시지일 경우 null
    private Status status;
    private String content;
    private String description;

    @Builder
    public ResponseMessageDTO(Status status, Long userId, String content, String description) {
        this.status = status;
        this.userId = userId;
        this.content = content;
        this.description = description;
    }
}
