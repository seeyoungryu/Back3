package com.example.withdogandcat.domain.chat.compig;

import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    /**
     * 웹소켓 연결을 요청하면 먼저 실행되서 검증함
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {

            try {
                final String token = jwtUtil.extractJwt(accessor);
                jwtUtil.validateToken(token);

            } catch (BaseException e) {
                // TODO 예외처리 필요
                log.error("토큰 오류: {}", e.getMessage());
            }
        }
        return message;
    }


}
