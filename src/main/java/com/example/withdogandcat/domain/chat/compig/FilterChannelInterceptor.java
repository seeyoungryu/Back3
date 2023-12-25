package com.example.withdogandcat.domain.chat.compig;

import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert headerAccessor != null;

        if (headerAccessor.getCommand() == StompCommand.CONNECT) {
            String token = headerAccessor.getFirstNativeHeader("Authorization");

            if (token != null) {
                try {
                    if (jwtUtil.validateToken(token)) {
                        String userEmail = jwtUtil.getUserEmailFromToken(token);
                        headerAccessor.addNativeHeader("User", userEmail);
                    }

                } catch (BaseException e) {
                    log.error("토큰 검증 중 오류 발생: {}", e.getMessage());
                }
            }
        }

        return message;
    }
}
