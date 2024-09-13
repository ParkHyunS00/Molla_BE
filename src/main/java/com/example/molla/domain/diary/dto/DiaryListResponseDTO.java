package com.example.molla.domain.diary.dto;

import com.example.molla.domain.common.Emotion;
import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class DiaryListResponseDTO {

    private Long diaryId;
    private String title;
    private String content;
    private Emotion diaryEmotion;
    private LocalDateTime createDate;
    private List<DiaryImageResponseDTO> images; // Base64 인코딩된 이미지들

    @Builder
    public DiaryListResponseDTO(Long diaryId, String title, String content, Emotion diaryEmotion, LocalDateTime createDate, List<DiaryImageResponseDTO> images) {
        this.diaryId = diaryId;
        this.title = title;
        this.content = content;
        this.diaryEmotion = diaryEmotion;
        this.createDate = createDate;
        this.images = images;
    }
}
