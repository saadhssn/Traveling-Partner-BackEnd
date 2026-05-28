package com.travelpartner.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelpartner.chat.websocket.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RealtimeNotificationService {

    private final WebSocketSessionManager sessionManager;
    private final ObjectMapper objectMapper;

    public void sendToUser(Long userId, Object payload) {
        try {
            WebSocketSession session = sessionManager.getSession(userId);

            if (session != null && session.isOpen()) {
                String json = objectMapper.writeValueAsString(payload);
                session.sendMessage(new TextMessage(json));
            }
        } catch (Exception e) {
            e.printStackTrace(); // later replace with logger
        }
    }
}