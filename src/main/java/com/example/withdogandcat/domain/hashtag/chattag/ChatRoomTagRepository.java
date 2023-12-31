package com.example.withdogandcat.domain.hashtag.chattag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomTagRepository extends JpaRepository<ChatRoomTag, Long> {
    Optional<ChatRoomTag> findByName(String name);

}
