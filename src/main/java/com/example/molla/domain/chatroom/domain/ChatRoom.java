package com.example.molla.domain.chatroom.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class ChatRoom {

    @Id @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    @Column(name = "create_date")
    private LocalDateTime createDate;
}
