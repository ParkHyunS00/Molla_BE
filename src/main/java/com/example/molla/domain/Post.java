package com.example.molla.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String content;

    @Column(name = "post_emotion")
    private String postEmotion;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "user_emotion")
    private String userEmotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
