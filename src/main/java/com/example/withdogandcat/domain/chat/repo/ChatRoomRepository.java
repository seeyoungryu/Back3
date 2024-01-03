package com.example.withdogandcat.domain.chat.repo;

import com.example.withdogandcat.domain.chat.entity.ChatRoom;
import com.example.withdogandcat.domain.chat.redis.RedisSubscriber;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
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

    private final RedisSubscriber redisSubscriber;
    private final RedisMessageListenerContainer redisMessageListener;
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new ConcurrentHashMap<>();
    }

    /**
     * 채팅방 생성
     */
    public void createChatRoom(String roomId, String name, Long creatorId) {

        if (name == null || name.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.ELEMENTS_IS_REQUIRED);
        }

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);
        chatRoom.setName(name);
        chatRoom.setCreatorId(creatorId);

        try {
            opsHashChatRoom.put(CHAT_ROOMS, roomId, chatRoom);
        } catch (Exception e) {
            log.error("채팅방 생성 오류: {}", roomId, e);
            throw new BaseException(BaseResponseStatus.REGISTRATION_FAILED);
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
     * 채팅방 삭제
     */
    public void deleteRoom(String roomId) {
        try {
            String key = "CHAT_ROOM";
            String field = roomId;
            redisTemplate.opsForHash().delete(key, field);
        } catch (Exception e) {
            log.error("레디스에서 채팅방 삭제 오류: {}", roomId, e);
        }
    }

    /**
     * 레디스에 채팅방 존재하는지 확인
     */
    public boolean existsById(String roomId) {
        return opsHashChatRoom.hasKey(CHAT_ROOMS, roomId);
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

}
