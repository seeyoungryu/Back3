package com.example.withdogandcat.domain.shop.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
public class ShopDetailResponseDto {

    private final ShopResponseDto shopResponseDto;
    private final List<ReviewResponseDto> reviews;

    @Builder
    public ShopDetailResponseDto(ShopResponseDto shopResponseDto, List<ReviewResponseDto> reviews) {
        this.shopResponseDto = shopResponseDto;
        this.reviews = reviews;
    }
}
