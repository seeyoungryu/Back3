package com.example.withdogandcat.domain.review.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {
    private Long reviewId;
    private Long id;
    private String nickname;
    private String review;
    private int likeCount;
    private LocalDateTime createdAt;

    public ReviewResponseDto(Long reviewId,Long id, String nickname, String review, int likeCount, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.id = id;
        this.nickname = nickname;
        this.review = review;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
    }
}
