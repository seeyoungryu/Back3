package com.example.withdogandcat.domain.shop.dto;

import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.shop.entity.ShopType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShopResponseDto {

    private final Long shopId;
    private final Long userId;
    private final String shopName;
    private final String shopTime;
    private final String shopTel;
    private final String shopAddress;
    private final ShopType shopType;
    private final String shopDescribe;
    private final String imageUrl;

    public ShopResponseDto(Long shopId, Long userId, String shopName, String shopTime, String shopTel,
                           String shopAddress, ShopType shopType, String shopDescribe, String imageUrl) {
        this.shopId = shopId;
        this.userId = userId;
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
                .userId(shop.getUser().getUserId())
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
