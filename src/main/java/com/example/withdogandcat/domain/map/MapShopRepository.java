package com.example.withdogandcat.domain.map;

import com.example.withdogandcat.domain.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MapShopRepository extends JpaRepository<MapShop, Long> {
    Optional<MapShop> findByShop(Shop shop);
}