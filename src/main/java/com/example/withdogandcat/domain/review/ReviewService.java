package com.example.withdogandcat.domain.review;

import com.example.withdogandcat.domain.review.dto.ReviewRequestDto;
import com.example.withdogandcat.domain.review.dto.ReviewResponseDto;
import com.example.withdogandcat.domain.review.entity.Review;
import com.example.withdogandcat.domain.review.like.LikeRepository;
import com.example.withdogandcat.domain.shop.repo.ShopRepository;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.user.UserRepository;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final LikeRepository likeRepository;

    /**
     * 리뷰 등록
     */
    @Transactional
    public BaseResponse<ReviewResponseDto> createReview(Long userId, Long shopId, @Valid ReviewRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        boolean alreadyReviewed = reviewRepository.existsByUserAndShop(user, shop);
        if (alreadyReviewed) {
            throw new BaseException(BaseResponseStatus.ALREADY_EXISTS);
        }

        if (shop.getUser().getUserId().equals(userId)) {
            throw new BaseException(BaseResponseStatus.OPERATION_NOT_ALLOWED);
        }

        Review review = Review.createReview(user, requestDto.getComment(), shop);
        reviewRepository.save(review);

        int likeCount = 0;

        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .userId(user.getUserId())
                .shopId(shop.getShopId())
                .nickname(user.getNickname())
                .comment(review.getComment())
                .likeCount(likeCount)
                .createdAt(review.getCreatedAt())
                .build();
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", responseDto);
    }

    /**
     * 모든 리뷰 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<ReviewResponseDto>> getAllReviews(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        List<Review> reviews = reviewRepository.findAllByShop(shop);
        List<ReviewResponseDto> responseDtos = reviews.stream()
                .map(review -> {
                    int likeCount = likeRepository.countByReview(review);
                    return ReviewResponseDto.builder()
                            .reviewId(review.getReviewId())
                            .userId(review.getUser().getUserId())
                            .shopId(review.getShop().getShopId())
                            .nickname(review.getUser().getNickname())
                            .comment(review.getComment())
                            .likeCount(likeCount)
                            .createdAt(review.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", responseDtos);
    }

    /**
     * 사용자 리뷰 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<ReviewResponseDto>> getUserReviewsByShop(Long shopId, Long userId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        List<Review> userReviews = reviewRepository.findByShopAndUser(shop, user);
        List<ReviewResponseDto> responseDtos = userReviews.stream()
                .map(review -> {
                    int likeCount = likeRepository.countByReview(review);
                    return ReviewResponseDto.builder()
                            .reviewId(review.getReviewId())
                            .userId(review.getUser().getUserId())
                            .shopId(review.getShop().getShopId())
                            .nickname(review.getUser().getNickname())
                            .comment(review.getComment())
                            .likeCount(likeCount)
                            .createdAt(review.getCreatedAt())
                            .isAuthor(true)
                            .build();
                })
                .collect(Collectors.toList());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", responseDtos);
    }


    /**
     * 리뷰 삭제
     */
    @Transactional
    public BaseResponse<Void> deleteReview(Long userId, Long shopId, Long reviewId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        Review review = reviewRepository.findByReviewIdAndShop(reviewId, shop)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.RETRIEVAL_FAILED));

        if (!review.getUser().getUserId().equals(userId)) {
            throw new BaseException(BaseResponseStatus.ACCESS_DENIED);
        }

        reviewRepository.deleteById(reviewId);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null);
    }
}
