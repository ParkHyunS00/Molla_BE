package com.example.molla.config;


import com.example.molla.domain.chatroom.handler.ChatWebSocketHandler;
import com.example.molla.domain.common.emotionAnalysis.EmotionAnalysisHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;
    private final EmotionAnalysisHandler emotionAnalysisHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        // 상담 메시지 관련 웹소켓
        registry.addHandler(chatWebSocketHandler, "ws/chat")
                .setAllowedOrigins("*");

        // 감정 분석 관련 웹소켓
        registry.addHandler(emotionAnalysisHandler, "/ws/emotion-analysis")
                .setAllowedOrigins("*");
    }
}
