package com.example.withdogandcat.domain.hashtag.shoptag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopTagRepository extends JpaRepository<ShopTag, Long> {
    Optional<ShopTag> findByName(String name);
}
