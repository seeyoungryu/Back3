package com.example.withdogandcat.domain.shop;

import com.example.mailtest.domain.shop.entity.Shop;
import com.example.mailtest.domain.shop.entity.ShopType;
import com.example.mailtest.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByUser(User user);
    List<Shop> findAllByShopType(ShopType shopType);
    void deleteByUser(User user);
    int countByUser(User user);
}
