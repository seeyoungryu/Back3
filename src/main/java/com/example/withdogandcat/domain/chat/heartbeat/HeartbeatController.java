package com.example.withdogandcat.domain.chat.heartbeat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HeartbeatController {

    /**
     * 채팅방 사용자의 비활성 상태를 확인하기 위한 컨트롤러
     */

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @MessageMapping("/chat/heartbeat")
    public void receiveHeartbeat(HeartbeatMessage message) {
        String userEmail = message.getUserEmail();
        long currentTime = System.currentTimeMillis();
        redisTemplate.opsForValue().set("heartbeat:" + userEmail, String.valueOf(currentTime));
    }

}
