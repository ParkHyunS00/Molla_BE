package com.example.molla.domain.post.dto;

import com.example.molla.domain.common.Emotion;
import com.example.molla.domain.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PostListResponseDTO {

    private Long postId;
    private String title;
    private String content;
    private Long commentCount;
    private Emotion userEmotion;
    private Long userEmotionCount;
    private String username;
    private LocalDateTime createDate;

    public PostListResponseDTO(Long postId, String title, String content, Emotion userEmotion,
                               Long userEmotionCount, Long commentCount, String username, LocalDateTime createDate) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.userEmotion = userEmotion;
        this.userEmotionCount = userEmotionCount;
        this.commentCount = commentCount;
        this.username = username;
        this.createDate = createDate;
    }
}
