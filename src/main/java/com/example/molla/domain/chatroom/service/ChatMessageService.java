package com.example.molla.domain.chatroom.service;

import com.example.molla.domain.chatroom.domain.ChatMessage;
import com.example.molla.domain.chatroom.dto.ChatHistoryResponseDTO;
import com.example.molla.domain.chatroom.dto.ChatMessageSendDTO;
import com.example.molla.domain.chatroom.repository.ChatMessageRepository;
import com.example.molla.domain.chatroom.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    @Transactional
    public Long saveMessage(ChatMessageSendDTO chatMessageSendDTO) {

        ChatRoom chatRoom = chatRoomService.getOrCreateChatRoom(chatMessageSendDTO.getUserId());

        ChatMessage chatMessage = chatMessageSendDTO.toEntity(chatRoom);
        return chatMessageRepository.save(chatMessage).getId();
    }

    public List<ChatHistoryResponseDTO> getChatHistory(Long userId) {

        ChatRoom chatRoom = chatRoomService.getOrCreateChatRoom(userId);

        return chatMessageRepository.findChatMessageByChatRoomId(chatRoom.getId());
    }
}
