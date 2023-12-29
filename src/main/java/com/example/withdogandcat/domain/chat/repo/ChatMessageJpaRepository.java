package com.example.withdogandcat.domain.chat.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, Long> {

    void deleteByRoomId(String roomId);

    @Modifying
    @Query("DELETE FROM ChatMessageEntity c WHERE c.sender.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);


    long countByRoomId(String roomId);

    @Query("SELECT cm FROM ChatMessageEntity cm WHERE cm.roomId = :roomId ORDER BY cm.createdAt ASC")
    List<ChatMessageEntity> findOldestMessages(@Param("roomId") String roomId);

    ChatMessageEntity findTopByRoomIdAndTypeOrderByIdDesc(String roomId, MessageType type);
}