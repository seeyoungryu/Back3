package com.example.withdogandcat.domain.map.dto;

import com.example.withdogandcat.domain.map.MapShop;

public class MapShopResponseDto {
    private Long shopId;
    private String address;
    private double latitude;
    private double longitude;

    public MapShopResponseDto(MapShop mapShop) {
        this.shopId = mapShop.getShop().getShopId();
        this.address = mapShop.getAddress();
        this.latitude = mapShop.getLatitude();
        this.longitude = mapShop.getLongitude();
    }
}