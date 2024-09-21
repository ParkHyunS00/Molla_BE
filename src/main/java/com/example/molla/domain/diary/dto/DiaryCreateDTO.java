package com.example.molla.domain.diary.dto;

import com.example.molla.domain.common.Emotion;
import com.example.molla.domain.diary.domain.Diary;
import com.example.molla.domain.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Getter @Setter
public class DiaryCreateDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Nullable
    private Emotion diaryEmotion;

    @NotNull
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
