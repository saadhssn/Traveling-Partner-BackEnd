package com.travelpartner.chat.websocket;

import com.travelpartner.config.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {

        try {

            String query = request.getURI().getQuery();

            if (query == null) {
                return false;
            }

            String token = null;

            for (String param : query.split("&")) {

                String[] pair = param.split("=");

                if (pair.length == 2 && pair[0].equals("token")) {
                    token = pair[1];
                }
            }

            if (token == null) {
                return false;
            }

            jwtService.validateToken(token);

            Long userId = jwtService.extractUserId(token);

            attributes.put("userId", userId);

            log.info("Authenticated websocket user: {}", userId);

            return true;

        } catch (Exception e) {

            log.error("WebSocket auth failed", e);

            return false;
        }
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {
    }
}