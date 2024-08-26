package com.example.molla.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Diary {

    @Id @GeneratedValue
    @Column(name = "diary_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_emotion")
    private Emotion diaryEmotion;

    @Column(name = "create_date")
    private LocalDateTime createDate;
}
