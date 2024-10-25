package com.example.molla.domain.common.emotionAnalysis.dto;

import com.example.molla.domain.common.Emotion;
import com.example.molla.domain.common.Status;
import com.example.molla.domain.emotion.dto.EmotionSumDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmotionAnalysisResponseDTO {
    private final Long userId; // ML 메시지일 경우 null
    private final Status status;
    private final Emotion result;
    private final String description;
    private final EmotionSumDTO emotionSum;

    @Builder
    public EmotionAnalysisResponseDTO(Status status, Long userId, Emotion result, String description, EmotionSumDTO emotionSum) {
        this.status = status;
        this.userId = userId;
        this.result = result;
        this.description = description;
        this.emotionSum = emotionSum;
    }
}
