package com.example.molla.domain.post.dto;

import com.example.molla.domain.common.Emotion;
import com.example.molla.domain.post.domain.Post;
import com.example.molla.domain.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PostCreateDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Emotion postEmotion;

    @NotNull
    private Long userId;

    public Post toEntity(LocalDateTime createDate, Emotion primaryUserEmotion, Long emotionCount, User user) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .postEmotion(this.postEmotion)
                .createDate(createDate)
                .userEmotion(primaryUserEmotion)
                .userEmotionCount(emotionCount)
                .user(user)
                .build();
    }
}
