package com.example.withdogandcat.domain.shop.repo;

import com.example.withdogandcat.domain.shop.entity.Shop;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepositoryCustom {
    List<Shop> searchShops(String keyword);
}
