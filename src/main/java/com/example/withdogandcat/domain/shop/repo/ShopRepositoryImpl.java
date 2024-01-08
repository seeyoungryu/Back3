package com.example.withdogandcat.domain.shop.repo;

import com.example.withdogandcat.domain.shop.entity.QShop;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ShopRepositoryImpl implements ShopRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 키워드 검색
     */
    @Override
    public List<Shop> searchShops(String keyword) {
        QShop shop = QShop.shop;
        return queryFactory.selectFrom(shop)
                .where(shop.shopName.contains(keyword)
                        .or(shop.shopAddress.contains(keyword))
                        .or(shop.shopDescribe.contains(keyword)))
                .fetch();
    }

}
