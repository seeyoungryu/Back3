package com.example.withdogandcat.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequestDto {

    @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
    private String comment;
}
