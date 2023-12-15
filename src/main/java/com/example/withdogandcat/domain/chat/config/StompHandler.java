package com.example.withdogandcat.domain.chat.config;

import com.example.withdogandcat.domain.chat.ChatRoomRepository;
import com.example.withdogandcat.domain.chat.ChatService;
import com.example.withdogandcat.domain.chat.model.ChatMessage;
import com.example.withdogandcat.global.exception.CustomException;
import com.example.withdogandcat.global.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String jwtToken = accessor.getFirstNativeHeader("Authorization");

        if (StompCommand.CONNECT == accessor.getCommand()) {
            log.info("CONNECT {}", jwtToken);
            try {
                jwtUtil.validateToken(jwtToken);
            } catch (CustomException e) {
                log.error("Token validation error: {}", e.getMessage());
                // TODO 예외 처리 로직 추가
            }
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            chatRoomRepository.setUserEnterInfo(sessionId, roomId);
            chatRoomRepository.plusUserCount(roomId);

            String name = Optional.ofNullable(jwtUtil.getUserInfoFromToken(jwtToken))
                    .map(Claims::getSubject).orElse("UnknownUser");
            chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).sender(name).build());
            log.info("SUBSCRIBED {}, {}", name, roomId);

        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);
            chatRoomRepository.minusUserCount(roomId);

            String name = Optional.ofNullable(jwtUtil.getUserInfoFromToken(jwtToken))
                    .map(Claims::getSubject).orElse("UnknownUser");
            chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.QUIT).roomId(roomId).sender(name).build());
            chatRoomRepository.removeUserEnterInfo(sessionId);
            log.info("DISCONNECTED {}, {}", sessionId, roomId);
        }
        return message;
    }
}
