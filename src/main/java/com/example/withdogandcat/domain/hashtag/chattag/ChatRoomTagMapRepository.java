package com.example.withdogandcat.domain.hashtag.chattag;

import com.example.withdogandcat.domain.chat.entity.ChatRoomEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomTagMapRepository extends JpaRepository<ChatRoomTagMap, Long> {

    List<ChatRoomTagMap> findByChatRoom(ChatRoomEntity chatRoom);
    Optional<ChatRoomTagMap> findByChatRoomAndChatRoomTag(ChatRoomEntity chatRoom, ChatRoomTag chatRoomTag);
    List<ChatRoomTagMap> findByChatRoomTag(ChatRoomTag chatRoomTag);
    long countByChatRoom(ChatRoomEntity chatRoom);
    long countByChatRoomTag(ChatRoomTag chatRoomTag);
    @Query("SELECT t.id, t.name, COUNT(t) as frequency FROM ChatRoomTag t JOIN ChatRoomTagMap c ON t.id = c.chatRoomTag.id GROUP BY t.name ORDER BY frequency DESC")
    List<Object[]> findChatRoomTagUsageFrequency(Pageable pageable);

}
