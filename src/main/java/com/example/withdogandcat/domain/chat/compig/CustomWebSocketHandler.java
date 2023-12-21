package com.example.withdogandcat.domain.chat.compig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

@Slf4j
public class CustomWebSocketHandler extends WebSocketHandlerDecorator {

    private RedisTemplate<String, Object> redisTemplate;

    /**
     * WebSocketHandlerDecorator 상속받는 사용자 정의 클래스
     * 사용자의 Session을 관리하기 위한 클래스
     */

    public CustomWebSocketHandler(WebSocketHandler delegate,
                                  RedisTemplate<String, Object> redisTemplate) {
        super(delegate);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String userEmail = (String) session.getAttributes().get("email");

        if (userEmail != null && !userEmail.isEmpty()) {
            String redisKey = "websocket_session:" + session.getId();
            redisTemplate.opsForValue().set(redisKey, userEmail);

            log.info("세션 열림: sessionId={}, userEmail={}", session.getId(), userEmail);

        } else {
            log.warn("이메일 못찾음={}", session.getId());
        }

        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

        String redisKey = "websocket_session:" + session.getId();
        redisTemplate.delete(redisKey);

        log.info("세션 닫힘: sessionId={}", session.getId());

        super.afterConnectionClosed(session, closeStatus);
    }

}
