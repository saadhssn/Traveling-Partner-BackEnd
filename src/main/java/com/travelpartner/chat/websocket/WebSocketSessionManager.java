package com.travelpartner.chat.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {

    private final ConcurrentHashMap<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void addSession(Long userId, WebSocketSession session) {

        WebSocketSession oldSession = sessions.get(userId);

        if (oldSession != null && oldSession.isOpen()) {
            try {
                oldSession.close();
            } catch (Exception ignored) {}
        }

        // ALWAYS replace (prevents duplicates)
        sessions.put(userId, session);
    }

    public WebSocketSession getSession(Long userId) {
        return sessions.get(userId);
    }

    public void removeSession(Long userId) {
        sessions.remove(userId);
    }
}