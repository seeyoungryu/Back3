package com.example.withdogandcat.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {

    private Long reviewId;
    private Long userId;
    private Long shopId;
    private String nickname;
    private String comment;
    private int likeCount;
    private LocalDateTime createdAt;
    private boolean isAuthor;
}
