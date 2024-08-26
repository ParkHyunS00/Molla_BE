package com.example.molla.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter // 테스트 위해 세터 열어둠
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;
}
