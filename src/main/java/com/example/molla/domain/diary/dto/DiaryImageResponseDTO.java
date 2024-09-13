package com.example.molla.domain.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DiaryImageResponseDTO {

    private Long imageId;
    private String base64EncodedImage;
}
