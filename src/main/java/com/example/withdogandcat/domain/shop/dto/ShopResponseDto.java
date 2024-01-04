package com.example.withdogandcat.domain.shop.dto;

import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.shop.entity.ShopType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ShopResponseDto {

    private final Long shopId;
    private final Long userId;
    private final String shopName;

    private final String shopStartTime;
    private final String shopEndTime;

    private final String shopTel1;
    private final String shopTel2;
    private final String shopTel3;

    private final String shopAddress;
    private final ShopType shopType;
    private final String shopDescribe;
    private final List<String> imageUrls;


    private final Double latitude;
    private final Double longitude;

    @Builder
    public ShopResponseDto(Long shopId, Long userId, String shopName, String shopStartTime,
                           String shopEndTime, String shopTel1, String shopTel2, String shopTel3,
                           String shopAddress, ShopType shopType, String shopDescribe,
                           List<String> imageUrls, double latitude, double longitude) {
        this.shopId = shopId;
        this.userId = userId;
        this.shopName = shopName;
        this.shopStartTime = shopStartTime;
        this.shopEndTime = shopEndTime;
        this.shopTel1 = shopTel1;
        this.shopTel2 = shopTel2;
        this.shopTel3 = shopTel3;
        this.shopAddress = shopAddress;
        this.shopType = shopType;
        this.shopDescribe = shopDescribe;
        this.imageUrls = imageUrls;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static ShopResponseDto from(Shop shop) {
        List<String> imageUrls = shop.getImages().stream()
                .map(image -> image.getStoredImagePath())
                .collect(Collectors.toList());

        return ShopResponseDto.builder()
                .shopId(shop.getShopId())
                .userId(shop.getUser().getUserId())
                .shopName(shop.getShopName())
                .shopStartTime(shop.getShopStartTime())
                .shopEndTime(shop.getShopEndTime())
                .shopTel1(shop.getShopTel1())
                .shopTel2(shop.getShopTel2())
                .shopTel3(shop.getShopTel3())
                .shopAddress(shop.getShopAddress())
                .shopType(shop.getShopType())
                .shopDescribe(shop.getShopDescribe())
                .latitude(shop.getLatitude())
                .longitude(shop.getLongitude())
                .imageUrls(imageUrls)
                .build();
    }
}
