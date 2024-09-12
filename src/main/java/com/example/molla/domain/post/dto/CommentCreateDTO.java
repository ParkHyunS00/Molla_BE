package com.example.molla.domain.post.dto;

import com.example.molla.domain.post.domain.Comment;
import com.example.molla.domain.post.domain.Post;
import com.example.molla.domain.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentCreateDTO {

    @NotNull
    private String content;

    @NotNull
    private Long userId;

    @NotNull
    private Long postId;

    public Comment toEntity(User user, Post post, LocalDateTime createDate) {
        return Comment.builder()
                .content(this.content)
                .user(user)
                .post(post)
                .createDate(createDate)
                .build();
    }
}
