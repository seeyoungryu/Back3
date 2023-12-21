package com.example.withdogandcat.domain.chat.repo;

import com.example.withdogandcat.domain.chat.entity.ChatRoomEntity;
import com.example.withdogandcat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {

    /**
     * Mysql 저장을 위한 인터페이스
     */
    Optional<ChatRoomEntity> findByRoomId(String roomId);
    void deleteByRoomId(String roomId);

    List<ChatRoomEntity> findByCreatorId(User creator);
}
