package com.example.withdogandcat.domain.shop.dto;

import com.example.withdogandcat.domain.review.dto.ReviewResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
public class ShopDetailResponseDto {

    private final ShopResponseDto shopResponseDto;
    private final List<ReviewResponseDto> reviews;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String reviewMessage;

    public ShopDetailResponseDto(ShopResponseDto shopResponseDto, List<ReviewResponseDto> reviews, String reviewMessage) {
        this.shopResponseDto = shopResponseDto;
        this.reviews = reviews;
        this.reviewMessage = reviewMessage;
    }
}
