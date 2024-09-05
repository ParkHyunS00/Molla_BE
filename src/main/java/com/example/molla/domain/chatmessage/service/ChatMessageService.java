package com.example.molla.domain.chatmessage.service;

import com.example.molla.domain.chatmessage.domain.ChatMessage;
import com.example.molla.domain.chatmessage.dto.ChatMessageSendDTO;
import com.example.molla.domain.chatmessage.repository.ChatMessageRepository;
import com.example.molla.domain.chatroom.domain.ChatRoom;
import com.example.molla.domain.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
