package com.example.withdogandcat.domain.review.like;

import com.example.withdogandcat.domain.review.ReviewRepository;
import com.example.withdogandcat.domain.review.entity.Review;
import com.example.withdogandcat.domain.user.UserRepository;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final LikeRepository reviewLikeRepository;

    /**
     * 좋아요 등록

     */
    @Transactional
    public void createLike(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.REVIEW_NOT_FOUND));

        reviewLikeRepository.findByUserAndReview(user, review)
                .ifPresent(like -> { throw new BaseException(BaseResponseStatus.ALREADY_LIKED); });

        reviewLikeRepository.save(new Like(user, review));
    }

    /**
     * 좋아요 상태 프론트에게 전달
     */
    @Transactional(readOnly = true)
    public boolean isLiked(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.REVIEW_NOT_FOUND));

        return reviewLikeRepository.findByUserAndReview(user, review).isPresent();
    }

    /**
     * 좋아요 취소
     */
    @Transactional
    public void deleteLike(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.REVIEW_NOT_FOUND));

        Like like = reviewLikeRepository.findByUserAndReview(user, review)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.LIKE_NOT_FOUND));

        reviewLikeRepository.delete(like);
    }

}
