package com.example.withdogandcat.domain.shop.dto;

import com.example.withdogandcat.domain.shop.entitiy.Shop;
import com.example.withdogandcat.domain.shop.entitiy.ShopType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ShopResponseDto {

    private final Long shopId;
    private final String shopName;
    private final String shopTime;
    private final String shopTel;
    private final String shopAddress;
    private final ShopType shopType;
    private final String shopDescribe;
    private final String imageUrl;

    @Builder
    public ShopResponseDto(Long shopId, String shopName, String shopTime, String shopTel,
                           String shopAddress, ShopType shopType, String shopDescribe,
                           String imageUrl) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopTime = shopTime;
        this.shopTel = shopTel;
        this.shopAddress = shopAddress;
        this.shopType = shopType;
        this.shopDescribe = shopDescribe;
        this.imageUrl = imageUrl;
    }

    public static ShopResponseDto from(Shop shop) {
        return ShopResponseDto.builder()
                .shopId(shop.getShopId())
                .shopName(shop.getShopName())
                .shopTime(shop.getShopTime())
                .shopTel(shop.getShopTel())
                .shopAddress(shop.getShopAddress())
                .shopType(shop.getShopType())
                .shopDescribe(shop.getShopDescribe())
                .imageUrl(shop.getImageUrl())
                .build();
    }
}
