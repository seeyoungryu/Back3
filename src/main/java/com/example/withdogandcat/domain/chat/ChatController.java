package com.example.withdogandcat.domain.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@RestController
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class ChatController {

    // TODO 채팅방 나가는 메세지 추가하기
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message) {
        if (ChatMessageDto.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        } else if (ChatMessageDto.MessageType.TALK.equals(message.getType())) {
            // TALK 타입 메시지는 추가 처리 없이 그대로 전송
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

}
