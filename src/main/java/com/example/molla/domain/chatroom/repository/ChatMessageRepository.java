package com.example.molla.domain.chatroom.repository;

import com.example.molla.domain.chatroom.domain.ChatMessage;
import com.example.molla.domain.chatroom.dto.ChatHistoryResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("select new com.example.molla.domain.chatroom.dto.ChatHistoryResponseDTO(c.message, c.isBot, c.sendDate) from ChatMessage c" +
            " where c.chatRoom.id = :roomId")
    List<ChatHistoryResponseDTO> findChatMessageByChatRoomId(Long roomId);
}
