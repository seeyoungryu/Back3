package com.example.withdogandcat.domain.chat.repo;

import com.example.withdogandcat.domain.chat.entity.ChatMessageEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, Long> {

    void deleteByRoomId(String roomId);
    // 특정 채팅방의 메시지 개수를 반환
    long countByRoomId(String roomId);

    @Query("SELECT m.id FROM ChatMessageEntity m WHERE m.roomId = :roomId ORDER BY m.createdAt ASC")
    List<Long> findOldestMessageIds(String roomId, Pageable pageable);

    // ID 목록을 사용하여 메시지 삭제
    @Modifying
    @Transactional
    void deleteByIdIn(List<Long> messageIds);

}
