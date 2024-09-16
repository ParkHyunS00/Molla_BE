package com.example.molla.domain.chatroom.service;

import com.example.molla.domain.chatroom.domain.ChatRoom;
import com.example.molla.domain.chatroom.repository.ChatRoomRepository;
import com.example.molla.domain.user.domain.User;
import com.example.molla.domain.user.repository.UserRepository;
import com.example.molla.exception.BusinessException;
import com.example.molla.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Cacheable(value = "chatRoomCache", key = "#userId")
    @Transactional
    public ChatRoom getOrCreateChatRoom(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return chatRoomRepository.findByUserId(userId)
                .orElseGet(() -> createNewChatRoom(user));
    }

    private ChatRoom createNewChatRoom(User user) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUser(user);
        chatRoom.setCreateDate(LocalDateTime.now());
        return chatRoomRepository.save(chatRoom);
    }
}
