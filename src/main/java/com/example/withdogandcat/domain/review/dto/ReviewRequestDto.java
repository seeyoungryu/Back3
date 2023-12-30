package com.example.withdogandcat.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequestDto {

    @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
    @Size(min = 2, max = 35, message = "댓글은 최소 2자 이상, 최대 35자 이하로 작성해주세요.")
    private String comment;
}
