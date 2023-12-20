package com.example.withdogandcat.domain.review;

import com.example.withdogandcat.domain.review.dto.ReviewRequestDto;
import com.example.withdogandcat.domain.review.dto.ReviewResponseDto;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shops/{shopId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("")
    public ResponseEntity<BaseResponse<ReviewResponseDto>> createReview(@PathVariable("shopId") Long shopId,
                                                                        @RequestBody ReviewRequestDto requestDto,
                                                                        Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getUserId();
        ReviewResponseDto responseDto = reviewService.createReview(userId, shopId, requestDto).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", responseDto));
    }

    @GetMapping("")
    public ResponseEntity<BaseResponse<List<ReviewResponseDto>>> getAllReviews(@PathVariable("shopId") Long shopId) {
        List<ReviewResponseDto> reviews = reviewService.getAllReviews(shopId).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", reviews));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<BaseResponse<Void>> deleteReview(@PathVariable("shopId") Long shopId,
                                                           @PathVariable("reviewId") Long reviewId,
                                                           Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getUserId();
        reviewService.deleteReview(userId, shopId, reviewId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null));
    }
}