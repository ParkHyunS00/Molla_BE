package com.example.molla.domain.chatroom.dto;

import com.example.molla.domain.common.Status;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseToMlMessageDTO {
    private Long userId; // ML 메시지일 경우 null
    private Status status;
    private List<ChatHistoryResponseDTO> content;
    private String description;

    @Builder
    public ResponseToMlMessageDTO(Status status, Long userId, List<ChatHistoryResponseDTO> content, String description) {
        this.status = status;
        this.userId = userId;
        this.content = content;
        this.description = description;
    }
}
