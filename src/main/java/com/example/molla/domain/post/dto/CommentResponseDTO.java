package com.example.molla.domain.post.dto;

import com.example.molla.domain.post.domain.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDTO {

    private Long id;
    private String content;
    private String username;

    public CommentResponseDTO(Long id, String content, String username) {
        this.id = id;
        this.content = content;
        this.username = username;
    }

    public static CommentResponseDTO fromEntity(Comment comment) {
        return new CommentResponseDTO(comment.getId(), comment.getContent(), comment.getUser().getUsername());
    }
}
