package com.example.withdogandcat.domain.review;

import com.example.withdogandcat.domain.review.dto.ReviewRequestDto;
import com.example.withdogandcat.domain.review.dto.ReviewResponseDto;
import com.example.withdogandcat.domain.review.entity.Review;
import com.example.withdogandcat.domain.review.like.LikeRepository;
import com.example.withdogandcat.domain.shop.ShopRepository;
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

        Review review = Review.createReview(user, requestDto.getComment(), shop);
        reviewRepository.save(review);

        ReviewResponseDto responseDto = new ReviewResponseDto(
                review.getReviewId(),
                user.getUserId(),
                shop.getShopId(),
                user.getNickname(),
                review.getComment(),
                0,
                review.getCreatedAt());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", responseDto);
    }

    @Transactional(readOnly = true)
    public BaseResponse<List<ReviewResponseDto>> getAllReviews(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        List<Review> reviews = reviewRepository.findAllByShop(shop);
        List<ReviewResponseDto> responseDtos = reviews.stream()
                .map(review -> {
                    int likeCount = likeRepository.countByReview(review);
                    return new ReviewResponseDto(
                            review.getReviewId(),
                            review.getUser().getUserId(),
                            review.getShop().getShopId(),
                            review.getUser().getNickname(),
                            review.getComment(),
                            likeCount,
                            review.getCreatedAt());
                })
                .collect(Collectors.toList());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", responseDtos);
    }

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
