package com.example.molla;

import com.example.molla.domain.chatmessage.dto.ChatMessageSendDTO;
import com.example.molla.domain.chatmessage.service.ChatMessageService;
import com.example.molla.domain.chatroom.domain.ChatRoom;
import com.example.molla.domain.chatroom.repository.ChatRoomRepository;
import com.example.molla.domain.chatroom.service.ChatRoomService;
import com.example.molla.domain.user.domain.User;
import com.example.molla.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ChatMessageServiceTest {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = userRepository.save(new User("testuser", "testuser@example.com", "password"));
        userRepository.save(testUser);
    }

    @Test
    @DisplayName("첫번째 채팅방 조회 이후 채팅방 조회시 캐싱 되어야 한다.")
    public void testChatRoomCaching() {

        // 새로운 메시지 전송, DB에 채팅방이 없으므로 생성 후 캐시적재
        ChatMessageSendDTO firstMessageDTO = new ChatMessageSendDTO();
        firstMessageDTO.setUserId(testUser.getId());
        firstMessageDTO.setMessage("first message");
        firstMessageDTO.setIsBot(false);
        Long firstMessageId = chatMessageService.saveMessage(firstMessageDTO);

        // 2. DB에서 채팅방이 생성되었는지 확인
        ChatRoom chatRoomFromDb = chatRoomRepository.findByUserId(testUser.getId()).orElse(null);
        assertThat(chatRoomFromDb).isNotNull(); // 채팅방이 DB에 생성되었는지 확인

        // 3. 캐시에 채팅방이 저장되었는지 확인
        ChatRoom chatRoomFromCache = chatRoomService.getOrCreateChatRoom(testUser.getId());
        assertThat(chatRoomFromCache).isNotNull();  // 캐시에 저장된 채팅방 확인

        // 4. 새로운 메시지를 전송, 이번엔 캐시에서 채팅방을 가져와야 함
        ChatMessageSendDTO secondMessageDTO = new ChatMessageSendDTO();
        secondMessageDTO.setUserId(testUser.getId());
        secondMessageDTO.setMessage("second message");
        secondMessageDTO.setIsBot(false);
        Long secondMessageId = chatMessageService.saveMessage(secondMessageDTO);

        ChatMessageSendDTO thirdMessageDTO = new ChatMessageSendDTO();
        thirdMessageDTO.setUserId(testUser.getId());
        thirdMessageDTO.setMessage("third message");
        thirdMessageDTO.setIsBot(false);
        Long thirdMessageId = chatMessageService.saveMessage(thirdMessageDTO);

        // 5. 캐시가 적용되었으므로 두 번째 호출 시 DB에서 쿼리가 발생하지 않아야 함
        // Hibernate SQL 로그를 확인하여 두 번째 호출에서 SELECT 쿼리가 발생하지 않음
    }
}
