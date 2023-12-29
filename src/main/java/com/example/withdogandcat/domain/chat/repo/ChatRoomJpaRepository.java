package com.example.withdogandcat.domain.chat.repo;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {

    Optional<ChatRoomEntity> findByRoomId(String roomId);
    void deleteByRoomId(String roomId);
    List<ChatRoomEntity> findByCreatorId(User creator);
    long countByCreatorId(User user);

}
