package com.travelpartner.rideplan.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class RideWebSocketHandler extends TextWebSocketHandler {

    private final RideSessionManager rideSessionManager;

    @Override
    public void afterConnectionEstablished(
            WebSocketSession session
    ) {

        Long userId =
                (Long) session.getAttributes().get("userId");

        rideSessionManager.addSession(userId, session);
    }

    @Override
    protected void handleTextMessage(
            WebSocketSession session,
            TextMessage message
    ) {

        // frontend handles incoming events
    }

    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            CloseStatus status
    ) {

        Long userId = getUserId(session);

        rideSessionManager.removeSession(userId);
    }

    private Long getUserId(WebSocketSession session) {

        String query = session.getUri().getQuery();

        if (query == null) {
            throw new RuntimeException("Query params missing");
        }

        for (String param : query.split("&")) {

            String[] pair = param.split("=");

            if (pair.length == 2 &&
                    pair[0].equals("userId")) {

                return Long.parseLong(pair[1]);
            }
        }

        throw new RuntimeException("userId not found");
    }
}