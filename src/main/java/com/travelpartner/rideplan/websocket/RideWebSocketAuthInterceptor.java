package com.travelpartner.rideplan.websocket;

import com.travelpartner.config.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class RideWebSocketAuthInterceptor
        implements HandshakeInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {

        try {

            String query =
                    request.getURI().getQuery();

            if (query == null) {
                return false;
            }

            String token = null;

            for (String param : query.split("&")) {

                String[] pair = param.split("=");

                if (pair.length == 2 &&
                        pair[0].equals("token")) {

                    token = pair[1];
                }
            }

            if (token == null) {
                return false;
            }

            jwtService.validateToken(token);

            Long userId =
                    jwtService.extractUserId(token);

            attributes.put("userId", userId);

            log.info(
                    "Ride websocket authenticated user: {}",
                    userId
            );

            return true;

        } catch (Exception e) {

            log.error(
                    "Ride websocket auth failed",
                    e
            );

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