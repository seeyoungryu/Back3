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

import static com.example.withdogandcat.global.security.jwt.JwtUtil.AUTHORIZATION_HEADER;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final JwtUtil jwtUtil;
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;
    private final RedisTemplate<String, String> redisTemplate; // RedisTemplate 주입


    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, StompHeaderAccessor headerAccessor) {

        String token = headerAccessor.getFirstNativeHeader(JwtUtil.AUTHORIZATION_HEADER);
        jwtUtil.validateToken(token);
        String userEmail = jwtUtil.getUserEmailFromToken(token);

        // 채팅방 존재 여부 확인
        if (!chatRoomRepository.existsById(message.getRoomId())) {
            // 채팅방이 존재하지 않는 경우 예외 처리
            throw new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND);
        }

        if (MessageType.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getRoomId());
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");

            // 사용자를 채팅방의 멤버로 추가하고, 사용자 수 증가
            redisTemplate.opsForSet().add("chatRoom:" + message.getRoomId() + ":members", userEmail);
            chatRoomRepository.incrementUserCount(message.getRoomId());

        } else if (MessageType.QUIT.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");

            // 사용자를 채팅방의 멤버에서 제거하고, 사용자 수 감소
            redisTemplate.opsForSet().remove("chatRoom:" + message.getRoomId() + ":members", userEmail);
            chatRoomRepository.decrementUserCount(message.getRoomId());

        } else if (MessageType.TALK.equals(message.getType())) {
            // TALK 메시지 처리 로직
        }

        // 모든 메시지 유형에 대해 토픽 발행 및 저장
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
        chatMessageService.saveMessage(message.getRoomId(), message, userEmail);
    }

}
