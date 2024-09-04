package com.example.molla.domain.post.dto;

import com.example.molla.domain.comment.dto.CommentResponseDTO;
import com.example.molla.domain.common.Emotion;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostDetailResponseDTO {

    private String title;
    private String content;
    private Emotion userEmotion;
    private Long userEmotionCount;
    private String username;
    private LocalDateTime createDate;
    private List<CommentResponseDTO> comments;

    @Builder
    public PostDetailResponseDTO(String title, String content, Emotion userEmotion,Long userEmotionCount,
                                 String username, LocalDateTime createDate, List<CommentResponseDTO> comments) {
        this.title = title;
        this.content = content;
        this.userEmotion = userEmotion;
        this.userEmotionCount = userEmotionCount;
        this.username = username;
        this.createDate = createDate;
        this.comments = comments;
    }
}
