package com.example.withdogandcat.domain.chat.repo;

import com.example.withdogandcat.domain.chat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {

    /**
     * Mysql 저장을 위한 클래스
     */
    Optional<ChatRoomEntity> findByRoomId(String roomId);
    void deleteByRoomId(String roomId);
}
