package com.example.withdogandcat.domain.chat.hashtag;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query("SELECT t.id, t.name, COUNT(t) as frequency FROM Tag t JOIN ChatRoomTagMap c ON t.id = c.tag.id GROUP BY t.name ORDER BY frequency DESC")
    List<Object[]> findTagUsageFrequency(Pageable pageable);

}
