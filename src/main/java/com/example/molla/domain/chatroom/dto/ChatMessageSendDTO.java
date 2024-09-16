package com.example.molla.domain.chatroom.dto;

import com.example.molla.domain.chatroom.domain.ChatMessage;
import com.example.molla.domain.chatroom.domain.ChatRoom;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Getter @Setter
public class ChatMessageSendDTO {

    @NotNull
    private Long userId;

    @NotNull
    private String message;

    @Nullable
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
