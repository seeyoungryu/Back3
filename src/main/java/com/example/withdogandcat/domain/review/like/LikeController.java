package com.example.withdogandcat.domain.review.like;

import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews/{reviewId}/like")
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createLike(@PathVariable("reviewId") Long reviewId, Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getUserId();
        likeService.createLike(userId, reviewId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null));
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> deleteLike(@PathVariable("reviewId") Long reviewId, Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getUserId();
        likeService.deleteLike(userId, reviewId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null));
    }
}