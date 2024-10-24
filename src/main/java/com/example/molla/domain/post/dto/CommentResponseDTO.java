package com.example.molla.domain.post.dto;

import com.example.molla.domain.post.domain.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponseDTO {

    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdDate;

    public static CommentResponseDTO fromEntity(Comment comment) {
        return new CommentResponseDTO(comment.getId(), comment.getContent(), comment.getUser().getUsername(), comment.getCreateDate());
    }
}
