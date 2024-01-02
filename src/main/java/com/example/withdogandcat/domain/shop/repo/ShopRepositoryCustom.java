package com.example.withdogandcat.domain.shop.repo;

import com.example.withdogandcat.domain.shop.entity.Shop;

import java.util.List;

public interface ShopRepositoryCustom {
    List<Shop> searchShops(String keyword);
}
