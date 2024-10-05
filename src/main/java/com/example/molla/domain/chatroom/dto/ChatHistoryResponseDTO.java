package com.example.molla.domain.chatroom.dto;

import com.example.molla.domain.chatroom.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatHistoryResponseDTO {

    private String message;
    private Boolean isBot;
    private LocalDateTime createDate;
}
