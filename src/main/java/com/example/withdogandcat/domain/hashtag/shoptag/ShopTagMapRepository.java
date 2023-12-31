package com.example.withdogandcat.domain.hashtag.shoptag;


import com.example.withdogandcat.domain.shop.entity.Shop;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopTagMapRepository extends JpaRepository<ShopTagMap, Long> {

    List<ShopTagMap> findByShop(Shop shop);

    Optional<ShopTagMap> findByShopAndShopTag(Shop shop, ShopTag shopTag);
    List<ShopTagMap> findByShopTag(ShopTag shopTag);
    long countByShop(Shop shop);
    long countByShopTag(ShopTag shopTag);

    @Query("SELECT t.id, t.name, COUNT(t) as frequency FROM ShopTag t JOIN ShopTagMap s ON t.id = s.shopTag.id GROUP BY t.name ORDER BY frequency DESC")
    List<Object[]> findShopTagUsageFrequency(Pageable pageable);

}
