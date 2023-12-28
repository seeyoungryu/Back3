package com.example.withdogandcat.domain.chat.repo;

import com.example.withdogandcat.domain.chat.entity.ChatMessageEntity;
import com.example.withdogandcat.domain.chat.entity.MessageType;
import io.lettuce.core.dynamic.annotation.Param;
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

    long countByRoomId(String roomId);

    @Query("SELECT cm FROM ChatMessageEntity cm WHERE cm.roomId = :roomId ORDER BY cm.createdAt ASC")
    List<ChatMessageEntity> findOldestMessages(@Param("roomId") String roomId);

    ChatMessageEntity findTopByRoomIdAndTypeOrderByIdDesc(String roomId, MessageType type);
}
