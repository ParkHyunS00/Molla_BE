package com.example.molla.domain.common.emotionAnalysis.dto;

import com.example.molla.domain.common.Emotion;
import lombok.Getter;

@Getter
public class EmotionAnalysisResultDTO {
    private Long userId;
    private Long targetId;
    private Emotion emotion;
    private String content;
    private String domain;
    private String timestamp;
}
