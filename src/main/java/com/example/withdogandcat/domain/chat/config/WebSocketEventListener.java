package com.example.withdogandcat.domain.chat.config;

import com.example.withdogandcat.domain.chat.entity.ChatMessage;
import com.example.withdogandcat.domain.chat.entity.MessageType;
import com.example.withdogandcat.domain.chat.redis.RedisPublisher;
import com.example.withdogandcat.domain.chat.repo.ChatRoomRepository;
import com.example.withdogandcat.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final JwtUtil jwtUtil;
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * websocket 연결 이벤트에 대한 세션관련 로직
     * connect 되면 세션 생성 -> 레디스 저장
     * disconnect 되면 세션 -> 레디스 삭제
     */

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String token = headerAccessor.getFirstNativeHeader(JwtUtil.AUTHORIZATION_HEADER);

        if (token != null && jwtUtil.validateToken(token, false)) {
            String userEmail = jwtUtil.getUserEmailFromToken(token);

            String activeSession = redisTemplate.opsForValue().get("active_session:" + userEmail);
            if (activeSession != null && !activeSession.equals(sessionId)) {
                throw new IllegalStateException("중복접속으로 새로운 세션으로 업데이트");
            }

            redisTemplate.opsForValue().set("active_session:" + userEmail, sessionId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        String sessionId = event.getSessionId();
        String userEmail = redisTemplate.opsForValue().get("websocket_session:" + sessionId);
        redisTemplate.delete("websocket_session:" + sessionId);
        redisTemplate.delete("websocket_session_jti:" + sessionId);

        String activeSession = redisTemplate.opsForValue().get("active_session:" + userEmail);
        if (activeSession != null && activeSession.equals(sessionId)) {
            redisTemplate.delete("active_session:" + userEmail);
        }
    }

    /**
     * 비활동 사용자 관련 로직
     */
    @Scheduled(fixedRate = 60000)
    public void removeInactiveUsers() {
        Set<String> users = redisTemplate.keys("heartbeat:*");
        long currentTime = System.currentTimeMillis();
        for (String userKey : users) {
            String userEmail = userKey.split(":")[1];
            String lastHeartbeatStr = redisTemplate.opsForValue().get(userKey);
            long lastHeartbeat = Long.parseLong(lastHeartbeatStr);

            if (currentTime - lastHeartbeat > 300000) { // 5분 초과 비활동
                Set<String> chatRooms = redisTemplate.keys("chatRoom:*:members");
                for (String chatRoomKey : chatRooms) {
                    boolean removed = redisTemplate.opsForSet().remove(chatRoomKey, userEmail) > 0;
                    if (removed) {
                        sendExitMessageToChatRoom(chatRoomKey, userEmail);
                    }
                }

                redisTemplate.delete("active_session:" + userEmail);
            }
        }
    }

    private void sendExitMessageToChatRoom(String chatRoomKey, String userEmail) {
        String roomId = chatRoomKey.split(":")[1];
        ChatMessage exitMessage = new ChatMessage();
        exitMessage.setSender("System");
        exitMessage.setType(MessageType.QUIT);
        exitMessage.setMessage(userEmail + " 장시간 비활동으로 퇴장 처리");
        exitMessage.setRoomId(roomId);

        redisPublisher.publish(chatRoomRepository.getTopic(roomId), exitMessage);
    }

}
