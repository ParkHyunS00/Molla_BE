package com.example.molla.domain.emotion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmotionSumDTO {
    private Long angrySum;
    private Long sadSum;
    private Long anxiousSum;
    private Long hurtSum;
    private Long happySum;
    private Long nothingSum;
}
