package com.travelpartner.chat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionManager sessionManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        Long userId =
                (Long) session.getAttributes().get("userId");

        sessionManager.addSession(userId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // frontend will handle
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = getUserId(session);
        sessionManager.removeSession(userId);
    }

//    private Long getUserId(WebSocketSession session) {
//        return Long.parseLong(session.getUri().getQuery().split("=")[1]);
//    }

    private Long getUserId(WebSocketSession session) {

        String query = session.getUri().getQuery();

        if (query == null) {
            throw new RuntimeException("Query params missing");
        }

        for (String param : query.split("&")) {

            String[] pair = param.split("=");

            if (pair.length == 2 && pair[0].equals("userId")) {
                return Long.parseLong(pair[1]);
            }
        }

        throw new RuntimeException("userId not found");
    }
}