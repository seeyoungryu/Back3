package com.example.withdogandcat.domain.shop;

import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByUser(User user);
}
