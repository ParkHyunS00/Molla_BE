package com.example.molla.domain.chatroom.controller;

import com.example.molla.common.StandardResponse;
import com.example.molla.domain.chatroom.dto.ChatHistoryResponseDTO;
import com.example.molla.domain.chatroom.dto.ChatMessageSendDTO;
import com.example.molla.domain.chatroom.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatMessageService chatMessageService;

    @GetMapping("/history/{id}")
    public ResponseEntity<StandardResponse<List<ChatHistoryResponseDTO>>> getChatHistory(@PathVariable("id") Long userId) {

        return StandardResponse.ofOk(chatMessageService.getChatHistory(userId));
    }
}
