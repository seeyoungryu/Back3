package com.example.withdogandcat.domain.shop.repo;

import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.shop.entity.ShopType;
import com.example.withdogandcat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long>, ShopRepositoryCustom{
    List<Shop> findByUser(User user);
    List<Shop> findAllByShopType(ShopType shopType);
    int countByUser(User user);
}
