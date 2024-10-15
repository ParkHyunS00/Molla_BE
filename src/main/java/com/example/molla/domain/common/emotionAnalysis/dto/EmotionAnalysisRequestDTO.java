package com.example.molla.domain.common.emotionAnalysis.dto;

import lombok.Getter;

@Getter
public class EmotionAnalysisRequestDTO {
    private Long userId;
    private Long targetId;
    private String content;
    private String domain;
}
