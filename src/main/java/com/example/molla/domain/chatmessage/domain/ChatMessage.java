package com.example.molla.domain.chatmessage.domain;

import com.example.molla.domain.chatroom.domain.ChatRoom;
import jakarta.persistence.*;
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
}
