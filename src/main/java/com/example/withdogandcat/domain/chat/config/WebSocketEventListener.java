package com.example.withdogandcat.domain.chat.config;

import com.example.withdogandcat.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String token = headerAccessor.getFirstNativeHeader(JwtUtil.AUTHORIZATION_HEADER);

        if (token != null && jwtUtil.validateToken(token, false)) {
            String userEmail = jwtUtil.getUserEmailFromToken(token);

            String activeSession = redisTemplate.opsForValue().get("active_session:" + userEmail);
            if (activeSession != null && !activeSession.equals(sessionId)) {
                throw new IllegalStateException("Already connected in another session");
            }

            redisTemplate.opsForValue().set("active_session:" + userEmail, sessionId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String userEmail = redisTemplate.opsForValue().get("websocket_session:" + sessionId);
        redisTemplate.delete("websocket_session:" + sessionId);
        redisTemplate.delete("websocket_session_jti:" + sessionId);

        String activeSession = redisTemplate.opsForValue().get("active_session:" + userEmail);
        if (activeSession != null && activeSession.equals(sessionId)) {
            redisTemplate.delete("active_session:" + userEmail);
        }
    }
}
