package com.example.molla.domain.chatmessage.dto;

import com.example.molla.domain.chatmessage.domain.ChatMessage;
import com.example.molla.domain.chatroom.domain.ChatRoom;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ChatMessageSendDTO {

    private Long userId;
    private String message;
    private Boolean isBot;

    public ChatMessage toEntity(ChatRoom chatRoom) {
        return ChatMessage.builder()
                .message(this.message)
                .isBot(this.isBot)
                .sendDate(LocalDateTime.now())
                .chatRoom(chatRoom)
                .build();
    }
}
