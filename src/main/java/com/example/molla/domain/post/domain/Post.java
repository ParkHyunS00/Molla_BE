package com.example.molla.domain.post.domain;

import com.example.molla.domain.common.Emotion;
import com.example.molla.domain.user.domain.User;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "post_emotion")
    private Emotion postEmotion;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_emotion")
    private Emotion userEmotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
