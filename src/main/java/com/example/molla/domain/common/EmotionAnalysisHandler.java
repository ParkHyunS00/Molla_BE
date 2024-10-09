package com.example.molla.domain.common;

import com.example.molla.domain.chatroom.exception.WebSocketException;
import com.example.molla.domain.diary.service.DiaryService;
import com.example.molla.domain.post.service.PostService;
import com.example.molla.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmotionAnalysisHandler extends TextWebSocketHandler {

    private final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private WebSocketSession mlSession = null;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PostService postService;
    private final DiaryService diaryService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String clientType = getQueryParameter(session, "client");

        if (clientType == null) {
            throw new WebSocketException(ErrorCode.WEBSOCKET_INVALID_TYPE);
        }

        if (clientType.equals("ml")) {
            mlSession = session;
            log.info("[{}] : ML connected for Emotion Analysis", session.getId());
        } else if (clientType.equals("user")) {
            String userId = getQueryParameter(session, "id");

            if (userId == null) {
                throw new WebSocketException(ErrorCode.WEBSOCKET_INVALID_SESSION);
            }

            userSessions.put(Long.valueOf(userId), session);
            log.info("[{}] : User connected for Emotion Analysis", session.getId());
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        if (session.equals(mlSession)) {
            EmotionAnalysisResultDTO emotionAnalysisResultDTO = objectMapper.readValue(payload, EmotionAnalysisResultDTO.class);
            handleMlResponse(emotionAnalysisResultDTO);
        } else {
            EmotionAnalysisRequestDTO emotionAnalysisRequestDTO = objectMapper.readValue(payload, EmotionAnalysisRequestDTO.class);
            handleUserRequest(session, emotionAnalysisRequestDTO);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (session.equals(mlSession)) {
            mlSession = null;
            log.info("[{}] : ML disconnected from Emotion Analysis", session.getId());
        } else {
            userSessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
            log.info("[{}] : User disconnected from Emotion Analysis", session.getId());
        }
    }

    private void handleUserRequest(WebSocketSession session, EmotionAnalysisRequestDTO emotionAnalysisRequestDTO) {
        if (mlSession == null || !mlSession.isOpen()) {
            sendErrorMessageToUser(session, ErrorCode.WEBSOCKET_INVALID_SESSION, "감정 분석 서버와 연결되어있지 않습니다.", emotionAnalysisRequestDTO.getUserId());
            return;
        }

        try {
            mlSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(emotionAnalysisRequestDTO)));
            log.info("[{}] Send Emotion Analysis request to ML", session.getId());
        } catch(Exception e) {
            throw new WebSocketException(ErrorCode.WEBSOCKET_MESSAGE_SEND_ERROR, e);
        }
    }

    private void sendErrorMessageToUser(WebSocketSession session, ErrorCode errorCode, String message, Long userId) {
        try {
            EmotionAnalysisResponseDTO response = EmotionAnalysisResponseDTO.builder()
                    .userId(userId)
                    .status(Status.ERROR)
                    .description(errorCode.getMessage() + message)
                    .build();

            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
            log.info("[{}] Send error message to user", message);
        } catch(Exception e) {
            log.error("[{}] Failed to send error message", e.getMessage());
        }
    }

    private void handleMlResponse(EmotionAnalysisResultDTO emotionAnalysisResultDTO) {
        Long userId = emotionAnalysisResultDTO.getUserId();
        WebSocketSession userSession = userSessions.get(userId);

        if (userSession == null || !userSession.isOpen()) {
            sendErrorMessageToMl("사용자의 세션이 연결되어 있지 않습니다. userId : " + userId);
            return;
        }

        try {
            EmotionAnalysisResponseDTO response = EmotionAnalysisResponseDTO.builder()
                    .userId(userId)
                    .status(Status.DONE)
                    .result(emotionAnalysisResultDTO.getEmotion())
                    .build();

            if (emotionAnalysisResultDTO.getDomain().equals("DIARY")) {
                diaryService.updateDiaryEmotion(emotionAnalysisResultDTO.getTargetId(), emotionAnalysisResultDTO.getEmotion());
            } else if (emotionAnalysisResultDTO.getDomain().equals("POST")) {
                postService.updatePostEmotion(emotionAnalysisResultDTO.getTargetId(), emotionAnalysisResultDTO.getEmotion());
            }

            userSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        } catch (Exception e) {
            log.error("Failed to send Emotion Analysis result to user : {}", e.getMessage());
        }
    }

    private void sendErrorMessageToMl(String message) {
        if (mlSession != null && mlSession.isOpen()) {
            try {
                EmotionAnalysisResponseDTO response = EmotionAnalysisResponseDTO.builder()
                        .status(Status.ERROR)
                        .description(message)
                        .build();
                mlSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
                log.error("[{}] Send error message to ML", message);
            } catch (Exception e) {
                log.error("[{}] Failed to send error message to ML", e.getMessage());
            }
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
