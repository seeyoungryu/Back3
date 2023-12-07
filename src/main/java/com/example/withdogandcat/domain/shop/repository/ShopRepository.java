package com.example.withdogandcat.domain.shop.repository;

import com.example.withdogandcat.domain.shop.entitiy.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
}
