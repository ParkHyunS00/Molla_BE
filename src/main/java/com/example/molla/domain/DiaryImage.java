package com.example.molla.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class DiaryImage {

    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;
}
