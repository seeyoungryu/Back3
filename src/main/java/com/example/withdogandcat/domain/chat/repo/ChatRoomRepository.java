package com.example.withdogandcat.domain.chat.repo;

import com.example.withdogandcat.domain.chat.entity.ChatRoom;
import com.example.withdogandcat.domain.chat.redis.RedisSubscriber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.Map;
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

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

}
