package com.example.withdogandcat.domain.chat.repo;

import com.example.mailtest.domain.chat.entity.ChatRoomEntity;
import com.example.mailtest.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
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
