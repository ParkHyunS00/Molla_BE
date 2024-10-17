package com.example.molla.domain.chatroom.handler;

import com.example.molla.domain.chatroom.dto.ChatHistoryResponseDTO;
import com.example.molla.domain.chatroom.dto.ChatMessageSendDTO;
import com.example.molla.domain.chatroom.dto.ResponseMessageDTO;
import com.example.molla.domain.chatroom.dto.ResponseToMlMessageDTO;
import com.example.molla.domain.common.Status;
import com.example.molla.domain.chatroom.exception.WebSocketException;
import com.example.molla.domain.chatroom.service.ChatMessageService;
import com.example.molla.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private WebSocketSession mlSession = null;
    private final ChatMessageService chatMessageService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<Long, AtomicBoolean> userMessageLock = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String clientType = getQueryParameter(session, "client");

        if (clientType == null) {
            throw new WebSocketException(ErrorCode.WEBSOCKET_INVALID_TYPE);
        }

        if (clientType.equals("ml")) {
            mlSession = session;
            log.info("[{}] : ML connected for counsel", session.getId());

        } else if (clientType.equals("user")) {
            String userId = getQueryParameter(session, "id");

            if (userId == null) {
                throw new WebSocketException(ErrorCode.WEBSOCKET_INVALID_SESSION);
            }

            userSessions.put(Long.valueOf(userId), session);
            userMessageLock.put(Long.valueOf(userId), new AtomicBoolean(true));
            log.info("[{}] : User connected for counsel", session.getId());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessageSendDTO chatMessageSendDTO = objectMapper.readValue(payload, ChatMessageSendDTO.class);

        if (session.equals(mlSession)) {
            handleMlMessage(chatMessageSendDTO);
        } else {
            Long userId = chatMessageSendDTO.getUserId();

            if (userId != null) {
                handleUserMessage(session, chatMessageSendDTO, userId);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (session.equals(mlSession)) {
            mlSession = null;
            log.info("[{}] : ML disconnected", session.getId());
        } else {
            userSessions.entrySet().removeIf(entry -> entry.getValue().equals(session));

            Long userId = getUserIdFromSession(session);
            if (userId != null) {
                userMessageLock.remove(userId);
                log.info("[{}] : User and associated message lock removed", userId);
            }

            log.info("[{}] : User disconnected", session.getId());
        }
    }

    private Long getUserIdFromSession(WebSocketSession session) {
        return userSessions.entrySet().stream()
                .filter(entry -> entry.getValue().equals(session))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private void handleMlMessage(ChatMessageSendDTO chatMessageSendDTO) {
        Long userId = chatMessageSendDTO.getUserId();
        WebSocketSession userSession = userSessions.get(userId);

        if (userSession == null || !userSession.isOpen()) {
            sendErrorMessageToMl("사용자의 세션이 연결되어 있지 않습니다. userId : " + userId);
            return;
        }

        try {
            ResponseMessageDTO responseMessage = ResponseMessageDTO.builder()
                    .userId(userId)
                    .status(Status.DONE)
                    .content(chatMessageSendDTO.getMessage())
                    .build();

            userSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(responseMessage)));

            Long messageId = chatMessageService.saveMessage(chatMessageSendDTO);
            userMessageLock.get(userId).set(true); // 사용자 메시지 전송 가능 상태

            log.info("[{}] : Message saved to {}", userSession.getId(), messageId);
        } catch(Exception e) {
            log.error("Failed to send ML message to user: {}", e.getMessage());
        }
    }

    private void sendErrorMessageToMl(String description) {
        if (mlSession != null && mlSession.isOpen()) {
            try {
                ResponseMessageDTO errorMessage = ResponseMessageDTO.builder()
                        .status(Status.ERROR)
                        .content(ErrorCode.WEBSOCKET_INVALID_SESSION.getMessage())
                        .description(description)
                        .build();

                mlSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(errorMessage)));
                log.info("[{}] Sent error message to ML", description);
            } catch (Exception e) {
                log.error("[{}] Failed to send error message to ML", e.getMessage());
            }
        }
    }


    private void handleUserMessage(WebSocketSession session, ChatMessageSendDTO chatMessageSendDTO, Long userId) {
        AtomicBoolean canSend = userMessageLock.get(userId);

        if (canSend != null && !canSend.get()) {
            sendErrorMessageToUser(session, ErrorCode.WEBSOCKET_INVALID_SEND, "상담사의 응답이 오기 전까지 메시지를 전송할 수 없습니다.", userId);
            return;
        }

        if (mlSession == null || !mlSession.isOpen()) {
            sendErrorMessageToUser(session, ErrorCode.WEBSOCKET_INVALID_SESSION, "상담사가 연결되어있지 않습니다.", userId);
            return;
        }

        try {
            Long messageId = chatMessageService.saveMessage(chatMessageSendDTO);
            log.info("[{}] : Message saved to {}", session.getId(), messageId);

            List<ChatHistoryResponseDTO> history = chatMessageService.getChatHistory(userId);
            ResponseToMlMessageDTO responseMessage = ResponseToMlMessageDTO.builder()
                    .userId(userId)
                    .status(Status.CHAT)
                    .content(history)
                    .build();

            canSend.set(false);
            mlSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(responseMessage)));
        } catch (Exception e) {
            throw new WebSocketException(ErrorCode.WEBSOCKET_MESSAGE_SEND_ERROR, e);
        }
    }

    private void sendErrorMessageToUser(WebSocketSession session, ErrorCode errorCode, String description, Long userId) {
        try {
            ResponseMessageDTO errorMessage = ResponseMessageDTO.builder()
                    .userId(userId)
                    .status(Status.ERROR)
                    .content(errorCode.getMessage())
                    .description(description)
                    .build();

            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(errorMessage)));
            log.info("[{}] Sent error message to client", description);
        } catch (Exception e) {
            log.error("[{}] Failed to send error message", e.getMessage());
        }
    }

    private String getQueryParameter(WebSocketSession session, String param) {

        String query = session.getUri().getQuery();

        if (query == null || !query.contains(param + "=")) {
            return null;
        }

        try {
            return query.split(param + "=")[1].split("&")[0];
        } catch(Exception e) {
            return null;
        }
    }
}
