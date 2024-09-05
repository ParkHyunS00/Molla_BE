package com.example.molla.domain.chatmessage.domain;

import com.example.molla.domain.chatroom.domain.ChatRoom;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class ChatMessage {

    @Id @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @Column(name = "send_date")
    private LocalDateTime sendDate;

    @Column(columnDefinition = "boolean default false", name = "is_bot")
    private Boolean isBot;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    public ChatMessage() {}

    @Builder
    public ChatMessage(LocalDateTime sendDate, Boolean isBot, String message, ChatRoom chatRoom) {
        this.sendDate = sendDate;
        this.isBot = isBot;
        this.message = message;
        this.chatRoom = chatRoom;
    }
}
