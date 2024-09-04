package com.example.molla.domain.diary.dto;

import com.example.molla.domain.common.Emotion;
import com.example.molla.domain.diary.domain.Diary;
import com.example.molla.domain.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class DiaryCreateDTO {

    private String title;
    private String content;
    private Emotion diaryEmotion;
    private LocalDateTime createDate;
    private Long userId;

    public Diary toEntity(User user, LocalDateTime createDate) {
        return Diary.builder()
                .title(this.title)
                .content(this.content)
                .diaryEmotion(this.diaryEmotion)
                .createDate(createDate)
                .user(user)
                .build();
    }
}
