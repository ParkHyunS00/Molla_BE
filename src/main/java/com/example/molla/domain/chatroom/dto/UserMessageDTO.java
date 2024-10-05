package com.example.molla.domain.chatroom.dto;

import lombok.Getter;

@Getter
public class UserMessageDTO {

    private final String userId;
    private final String message;
    private final String messageType;
    private final Boolean isBot;

    public UserMessageDTO(String userId, String message, String messageType, Boolean isBot) {
        this.userId = userId;
        this.message = message;
        this.messageType = messageType;
        this.isBot = isBot;
    }
}
