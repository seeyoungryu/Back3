package com.example.withdogandcat.domain.shop.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ShopResponseDto {

    private final Long shopId;
    private final Long userId;
    private final String shopName;
    private final String shopTime;
    private final String shopTel;
    private final String shopAddress;
    private final ShopType shopType;
    private final String shopDescribe;
    private final List<String> imageUrls;

    @Builder
    public ShopResponseDto(Long shopId, Long userId, String shopName, String shopTime,
                           String shopTel, String shopAddress, ShopType shopType,
                           String shopDescribe, List<String> imageUrls) {
        this.shopId = shopId;
        this.userId = userId;
        this.shopName = shopName;
        this.shopTime = shopTime;
        this.shopTel = shopTel;
        this.shopAddress = shopAddress;
        this.shopType = shopType;
        this.shopDescribe = shopDescribe;
        this.imageUrls = imageUrls;
    }

    public static ShopResponseDto from(Shop shop) {

        List<String> imageUrls = shop.getImages().stream()
                .map(image -> image.getStoredImagePath())
                .collect(Collectors.toList());

        return ShopResponseDto.builder()
                .shopId(shop.getShopId())
                .userId(shop.getUser().getUserId())
                .shopName(shop.getShopName())
                .shopTime(shop.getShopTime())
                .shopTel(shop.getShopTel())
                .shopAddress(shop.getShopAddress())
                .shopType(shop.getShopType())
                .shopDescribe(shop.getShopDescribe())
                .imageUrls(imageUrls)
                .build();
    }
}
