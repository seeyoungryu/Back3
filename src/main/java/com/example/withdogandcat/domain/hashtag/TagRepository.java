package com.example.withdogandcat.domain.hashtag;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    // 채팅방 태그 사용 빈도
    @Query("SELECT t.id, t.name, COUNT(t) as frequency FROM Tag t JOIN ChatRoomTagMap c ON t.id = c.tag.id GROUP BY t.name ORDER BY frequency DESC")
    List<Object[]> findChatRoomTagUsageFrequency(Pageable pageable);

    // 상점 태그 사용 빈도
    @Query("SELECT t.id, t.name, COUNT(t) as frequency FROM Tag t JOIN ShopTagMap s ON t.id = s.tag.id GROUP BY t.name ORDER BY frequency DESC")
    List<Object[]> findShopTagUsageFrequency(Pageable pageable);
}