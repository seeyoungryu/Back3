package com.example.withdogandcat.domain.map.dto;

import lombok.Getter;

@Getter
public class MapShopRequestDto {
    @Getter
    private Long shopId;
    private String address;
    private double latitude;
    private double longitude;


    public MapShopRequestDto(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}