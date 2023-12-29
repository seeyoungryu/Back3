package com.example.withdogandcat.domain.chat.hashtag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomTagMapRepository extends JpaRepository<ChatRoomTagMap, Long> {
    List<ChatRoomTagMap> findByChatRoom(ChatRoomEntity chatRoom);
    Optional<ChatRoomTagMap> findByChatRoomAndTag(ChatRoomEntity chatRoom, Tag tag);
    List<ChatRoomTagMap> findByTag(Tag tag);
    long countByChatRoom(ChatRoomEntity chatRoom);
    long countByTag(Tag tag);
}
