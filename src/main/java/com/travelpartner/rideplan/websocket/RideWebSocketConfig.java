package com.travelpartner.rideplan.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class RideWebSocketConfig
        implements WebSocketConfigurer {

    private final RideWebSocketHandler handler;

    private final RideWebSocketAuthInterceptor interceptor;

    @Override
    public void registerWebSocketHandlers(
            WebSocketHandlerRegistry registry
    ) {

        registry.addHandler(handler, "/ws/rides")
                .addInterceptors(interceptor)
                .setAllowedOriginPatterns("*");
    }
}