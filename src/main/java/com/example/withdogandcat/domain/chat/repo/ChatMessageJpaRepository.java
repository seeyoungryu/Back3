package com.example.withdogandcat.domain.chat.repo;

import com.example.withdogandcat.domain.chat.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageJpaRepository extends JpaRepository <ChatMessageEntity, Long> {

    /**
     * Mysql 저장을 위한 클래스
     */
    void deleteByRoomId(String roomId);
}
