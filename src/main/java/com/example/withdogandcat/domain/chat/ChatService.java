package com.example.withdogandcat.domain.chat;

import com.example.withdogandcat.domain.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * destination정보에서 roomId 추출
     */
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    /**
     * 채팅방에 메시지 발송
     */
    public void sendChatMessage(ChatMessage chatMessage) {
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            // 유저가 채팅방에 입장하는 경우, 유저 수 증가
            chatRoomRepository.plusUserCount(chatMessage.getRoomId());
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");

        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            // 유저가 채팅방에서 퇴장하는 경우, 유저 수 감소
            chatRoomRepository.minusUserCount(chatMessage.getRoomId());
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[알림]");
        }

        // 채팅방의 현재 유저 수 설정
        chatMessage.setUserCount(chatRoomRepository.getUserCount(chatMessage.getRoomId()));

        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }
}
