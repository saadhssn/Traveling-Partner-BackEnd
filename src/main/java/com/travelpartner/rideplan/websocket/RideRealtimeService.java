package com.travelpartner.rideplan.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
public class RideRealtimeService {

    private final RideSessionManager rideSessionManager;

    private final ObjectMapper objectMapper;

    public void sendToUser(
            Long userId,
            Object payload
    ) {

        try {

            WebSocketSession session =
                    rideSessionManager.getSession(userId);

            if (session != null && session.isOpen()) {

                String json =
                        objectMapper.writeValueAsString(payload);

                session.sendMessage(
                        new TextMessage(json)
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}