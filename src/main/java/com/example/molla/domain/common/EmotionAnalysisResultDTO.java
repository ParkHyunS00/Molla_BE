package com.example.molla.domain.common;

import lombok.Getter;

@Getter
public class EmotionAnalysisResultDTO {
    private Long userId;
    private Long targetId;
    private Emotion emotion;
    private String domain;
}
