package com.example.withdogandcat.domain.chat.service;

import com.example.withdogandcat.domain.chat.entity.ChatMessage;
import com.example.withdogandcat.domain.chat.entity.ChatMessageEntity;
import com.example.withdogandcat.domain.chat.repo.ChatMessageJpaRepository;
import com.example.withdogandcat.domain.user.UserRepository;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatMessageJpaRepository chatMessageJpaRepository;

    // 채팅방 메시지 저장
    @Transactional
    public BaseResponse<Void> saveMessage(String roomId, ChatMessage chatMessage, String userEmail) {
        String redisKey = "chatRoom:" + roomId + ":messages";
        Long redisSize = redisTemplate.opsForList().size(redisKey);
        int redisMaxSize = 20;

        if (redisSize != null && redisSize >= redisMaxSize) {
            redisTemplate.opsForList().leftPop(redisKey);
        }

        redisTemplate.opsForList().rightPush(redisKey, chatMessage);

        long dbMaxSize = 200; // 저장할 최대 메시지 수
        long dbSize = chatMessageJpaRepository.countByRoomId(roomId);

        if (dbSize >= dbMaxSize) {
            List<Long> oldestMessageIds = chatMessageJpaRepository.findOldestMessageIds(
                    roomId,
                    PageRequest.of(0, 1)
            );
            chatMessageJpaRepository.deleteByIdIn(oldestMessageIds);
        }

        User sender = userRepository.findByEmail(userEmail).orElseThrow();
        ChatMessageEntity chatMessageEntity = convertToEntity(chatMessage, sender);
        chatMessageJpaRepository.save(chatMessageEntity);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null);
    }

    @Transactional
    public BaseResponse<Void> deleteMessages(String roomId) {
        String key = "chatRoom:" + roomId + ":messages";
        redisTemplate.delete(key);

        chatMessageJpaRepository.deleteByRoomId(roomId);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null);
    }

    // 채팅방에 저장된 메세지 가져오기
    public BaseResponse<List<Object>> getMessages(String roomId) {
        String key = "chatRoom:" + roomId + ":messages";
        List<Object> messages = redisTemplate.opsForList().range(key, 0, -1);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", messages);
    }

    private ChatMessageEntity convertToEntity(ChatMessage chatMessage, User sender) {
        return ChatMessageEntity.builder()
                .type(chatMessage.getType())
                .roomId(chatMessage.getRoomId())
                .sender(sender)
                .message(chatMessage.getMessage())
                .build();
    }

}
