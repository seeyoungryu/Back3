package com.example.withdogandcat.domain.chat.config;

import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class FilterChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert headerAccessor != null;

        if (headerAccessor.getCommand() == StompCommand.CONNECT) {
            String token = headerAccessor.getFirstNativeHeader("Authorization");

            if (token != null) {
                try {
                    if (jwtUtil.validateToken(token, false)) {
                        String userEmail = jwtUtil.getUserEmailFromToken(token);
                        String storedActiveSession = redisTemplate.opsForValue().get("active_session:" + userEmail);

                        if (storedActiveSession != null && !storedActiveSession.equals(headerAccessor.getSessionId())) {
                            throw new IllegalStateException("중복접속으로 새로운 세션으로 업데이트");
                        }

                        headerAccessor.addNativeHeader("User", userEmail);
                    }

                } catch (BaseException e) {
                    log.error("토큰 검증 중 오류 발생: {}", e.getMessage());
                    return null;
                }
            }
        }
        return message;
    }
}
