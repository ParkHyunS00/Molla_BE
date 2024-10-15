package com.example.molla.domain.common.emotionAnalysis.dto;

import com.example.molla.domain.common.Emotion;
import com.example.molla.domain.common.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmotionAnalysisResponseDTO {
    private Long userId; // ML 메시지일 경우 null
    private Status status;
    private Emotion result;
    private String description;

    @Builder
    public EmotionAnalysisResponseDTO(Status status, Long userId, Emotion result, String description) {
        this.status = status;
        this.userId = userId;
        this.result = result;
        this.description = description;
    }
}
