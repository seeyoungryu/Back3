package com.example.withdogandcat.domain.chat.controller;

import com.example.withdogandcat.domain.chat.entity.ChatMessage;
import com.example.withdogandcat.domain.chat.entity.MessageType;
import com.example.withdogandcat.domain.chat.redis.RedisPublisher;
import com.example.withdogandcat.domain.chat.repo.ChatRoomRepository;
import com.example.withdogandcat.domain.chat.service.ChatMessageService;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final JwtUtil jwtUtil;
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;
    private final RedisTemplate<String, String> redisTemplate;


    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, StompHeaderAccessor headerAccessor) {

        String token = headerAccessor.getFirstNativeHeader(JwtUtil.AUTHORIZATION_HEADER);
        jwtUtil.validateToken(token, false);
        String userEmail = jwtUtil.getUserEmailFromToken(token);

        long currentTime = System.currentTimeMillis();
        redisTemplate.opsForValue().set("heartbeat:" + userEmail, String.valueOf(currentTime));

        if (!chatRoomRepository.existsById(message.getRoomId())) {
            throw new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND);
        }

        if (MessageType.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getRoomId());
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");

            redisTemplate.opsForSet().add("chatRoom:" + message.getRoomId() + ":members", userEmail);

        } else if (MessageType.QUIT.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");

            redisTemplate.opsForSet().remove("chatRoom:" + message.getRoomId() + ":members", userEmail);

        } else if (MessageType.TALK.equals(message.getType())) {
            if (message.getMessage() == null || message.getMessage().trim().isEmpty()) {
                throw new BaseException(BaseResponseStatus.ELEMENTS_IS_REQUIRED);
            }
        }

        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
        chatMessageService.saveMessage(message.getRoomId(), message, userEmail);
    }

}
