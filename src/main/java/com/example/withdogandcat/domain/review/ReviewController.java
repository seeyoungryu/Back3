package com.example.withdogandcat.domain.review;

import com.example.withdogandcat.domain.review.dto.ReviewRequestDto;
import com.example.withdogandcat.domain.review.dto.ReviewResponseDto;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shops/{shopId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 등록
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BaseResponse<ReviewResponseDto>> createReview(@PathVariable("shopId") Long shopId,
                                                                        @Valid @RequestBody ReviewRequestDto requestDto,
                                                                        Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getUserId();
        ReviewResponseDto responseDto = reviewService.createReview(userId, shopId, requestDto).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", responseDto));
    }

    /**
     * 모든 리뷰 조회
     */
    @GetMapping("")
    public ResponseEntity<BaseResponse<List<ReviewResponseDto>>> getAllReviews(@PathVariable("shopId") Long shopId) {
        List<ReviewResponseDto> reviews = reviewService.getAllReviews(shopId).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", reviews));
    }

    /**
     * 사용자가 작성한 리뷰 조회
     */
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BaseResponse<List<ReviewResponseDto>>> getUserReviewsByShop(@PathVariable("shopId") Long shopId,
                                                                                      Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getUserId();
        List<ReviewResponseDto> reviews = reviewService.getUserReviewsByShop(shopId, userId).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", reviews));
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BaseResponse<Void>> deleteReview(@PathVariable("shopId") Long shopId,
                                                           @PathVariable("reviewId") Long reviewId,
                                                           Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getUserId();
        reviewService.deleteReview(userId, shopId, reviewId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null));
    }

}
