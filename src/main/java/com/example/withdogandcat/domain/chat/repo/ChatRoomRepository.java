package com.example.withdogandcat.domain.chat.repo;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {

    /**
     * Redis 저장을 위한 클래스
     */

    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new ConcurrentHashMap<>();
    }

    public long count() {
        try {
            return opsHashChatRoom.size(CHAT_ROOMS);
        } catch (Exception e) {
            log.error("채팅방 카운트 오류", e);
            return 0;
        }
    }

    /**
     * 채팅방 생성
     */
    public void createChatRoom(String roomId, String name, Long creatorId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);
        chatRoom.setName(name);
        chatRoom.setCreatorId(creatorId);
        try {
            opsHashChatRoom.put(CHAT_ROOMS, roomId, chatRoom);
        } catch (Exception e) {
            log.error("생성 오류: {}", name, e);
        }
    }

    /**
     * 채팅방 입장 + Topic 발행
     */
    public void enterChatRoom(String roomId) {
        topics.computeIfAbsent(roomId, k -> {
            ChannelTopic topic = new ChannelTopic(roomId);
            try {
                redisMessageListener.addMessageListener(redisSubscriber, topic);
            } catch (Exception e) {
                log.error("채팅방 입장 오류: {}", roomId, e);
            }
            return topic;
        });
    }

    /**
     * 유저당 활성화 하는 채팅방 개수
     */
    public long countActiveRoomsByUserId(Long userId) {
        long activeRoomsCount = 0;
        try {
            Set<Object> roomIds = redisTemplate.opsForHash().keys(CHAT_ROOMS);
            if (roomIds != null) {
                for (Object roomId : roomIds) {
                    Boolean isMember = redisTemplate.opsForSet().isMember("chatRoom:" + roomId + ":members", userId.toString());
                    if (isMember != null && isMember) {
                        activeRoomsCount++;
                    }
                }
            }
            return activeRoomsCount;
        } catch (Exception e) {
            log.error("유저별 활성화된 채팅방 수 계산 오류: {}", userId, e);
            return 0;
        }
    }

    /**
     * 채팅방 인원수 증가/감소
     */
    public void incrementUserCount(String roomId) {
        redisTemplate.opsForValue().increment("chatRoom:" + roomId + ":userCount");
    }

    public void decrementUserCount(String roomId) {
        Long count = redisTemplate.opsForValue().increment("chatRoom:" + roomId + ":userCount", -1);
        if (count != null && count < 0) {
            redisTemplate.opsForValue().set("chatRoom:" + roomId + ":userCount", "0");
        }
    }

    /**
     * 채팅방 삭제
     */
    public void deleteRoom(String roomId) {
        try {
            String key = "CHAT_ROOM";
            String field = roomId; // 채팅방 ID가 field로 사용됨
            redisTemplate.opsForHash().delete(key, field); // 특정 field 삭제
        } catch (Exception e) {
            log.error("레디스에서 채팅방 삭제 오류: {}", roomId, e);
        }
    }

    // 채팅방이 Redis에 존재하는지 확인
    public boolean existsById(String roomId) {
        return opsHashChatRoom.hasKey(CHAT_ROOMS, roomId);
    }

    public long getUserCount(String roomId) {
        String key = "chatRoom:" + roomId + ":userCount";
        Object countObj = redisTemplate.opsForValue().get(key);
        if (countObj != null) {
            String countStr = String.valueOf(countObj);
            return Long.parseLong(countStr);
        }
        return 0;
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

}
