package com.example.withdogandcat.domain.chat.controller;

import com.example.withdogandcat.domain.chat.ChatRoomRepository;
import com.example.withdogandcat.domain.chat.ChatService;
import com.example.withdogandcat.domain.chat.model.ChatMessage;
import com.example.withdogandcat.global.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;
    private final JwtUtil jwtUtil;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("Authorization") String token) {
        String nickname = Optional.ofNullable(jwtUtil.getUserInfoFromToken(token))
                .map(Claims::getSubject).orElse("UnknownUser");

        // 로그인 회원 정보로 대화명 설정
        message.setSender(nickname);

        // 채팅방 인원수 세팅
        message.setUserCount(chatRoomRepository.getUserCount(message.getRoomId()));
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        chatService.sendChatMessage(message);
    }
}
