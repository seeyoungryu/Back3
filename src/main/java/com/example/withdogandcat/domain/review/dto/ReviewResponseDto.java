package com.example.withdogandcat.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {

    private Long reviewId;
    private Long userId;
    private Long shopId;
    private String nickname;
    private String comment;
    private int likeCount;
    private LocalDateTime createdAt;

    @Builder
    public ReviewResponseDto(Long reviewId, Long userId, Long shopId, String nickname,
                             String comment, int likeCount, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.shopId = shopId;
        this.nickname = nickname;
        this.comment = comment;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
    }

}
