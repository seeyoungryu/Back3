package com.example.withdogandcat.domain.chat.service;

import com.example.mailtest.domain.chat.entity.ChatMessage;
import com.example.mailtest.domain.chat.entity.ChatMessageEntity;
import com.example.mailtest.domain.chat.entity.MessageType;
import com.example.mailtest.domain.chat.repo.ChatMessageJpaRepository;
import com.example.mailtest.domain.user.UserRepository;
import com.example.mailtest.domain.user.entity.User;
import com.example.mailtest.global.common.BaseResponse;
import com.example.mailtest.global.exception.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
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

        // 데이터베이스에 메시지 저장 전에 오래된 메시지 삭제
        int maxMessagesPerRoom = 30; // 각 채팅방당 최대 메시지 수

        // 데이터베이스에 새 메시지 저장
        User sender = userRepository.findByEmail(userEmail).orElseThrow();
        ChatMessageEntity chatMessageEntity = convertToEntity(chatMessage, sender);
        chatMessageJpaRepository.save(chatMessageEntity);

        // 각 채팅방의 메시지 수가 최대치를 초과할 경우 오래된 메시지 삭제
        long dbSize = chatMessageJpaRepository.countByRoomId(roomId);
        if (dbSize >= maxMessagesPerRoom) {
            List<ChatMessageEntity> messagesToDeleteList = chatMessageJpaRepository.findOldestMessages(roomId);
            chatMessageJpaRepository.deleteAll(messagesToDeleteList);
        }

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

    // 각 채팅방의 최신 TALK 메시지를 가져오는 메서드
    public ChatMessage getLastTalkMessage(String roomId) {
        ChatMessageEntity messageEntity = chatMessageJpaRepository.findTopByRoomIdAndTypeOrderByIdDesc(roomId, MessageType.TALK);
        if (messageEntity == null) {
            return null;
        }
        return convertEntityToDto(messageEntity);
    }

    // ChatMessageEntity를 ChatMessage로 변환하는 메서드
    private ChatMessage convertEntityToDto(ChatMessageEntity entity) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(entity.getType());
        chatMessage.setRoomId(entity.getRoomId());
        chatMessage.setMessage(entity.getMessage());

        // User 엔티티의 특정 속성(예: email 또는 nickname)을 sender로 설정
        if (entity.getSender() != null) {
            chatMessage.setSender(entity.getSender().getEmail());
        }

        return chatMessage;
    }


    @Transactional
    public void deleteAllMessagesByUser(Long userId) {
        chatMessageJpaRepository.deleteByUserId(userId);
    }


}
