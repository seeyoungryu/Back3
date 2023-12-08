package com.example.withdogandcat.domain.shop;

import com.example.withdogandcat.domain.review.dto.ReviewResponseDto;
import com.example.withdogandcat.domain.review.like.LikeRepository;
import com.example.withdogandcat.domain.shop.dto.ShopDetailResponseDto;
import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.shop.dto.ShopResponseDto;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.config.s3.S3Upload;
import com.example.withdogandcat.global.dto.ApiResponseDto;
import com.example.withdogandcat.global.tool.LoginAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.example.withdogandcat.domain.review.ReviewRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final S3Upload s3Upload;
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;

    private static final String SHOP_BUCKET = "shop-pet";

    // 마이페이지 가게 조회
    public ApiResponseDto<List<ShopResponseDto>> getShopsByUserId(Long userId) {
        List<Shop> shops = shopRepository.findByUser_UserId(userId);

        String message = shops.isEmpty() ? "등록된 가게가 없습니다" : "가게 목록 조회 성공";
        List<ShopResponseDto> shopDtos = shops.stream()
                .map(ShopResponseDto::from)
                .collect(Collectors.toList());

        return new ApiResponseDto<>(message, shopDtos);
    }

    // 가게 등록
    @Transactional
    public ShopResponseDto createShop(ShopRequestDto shopRequestDto, MultipartFile imageUrl,
                                      @LoginAccount User currentUser) throws IOException {
        String image = s3Upload.upload(imageUrl, SHOP_BUCKET);
        Shop shop = Shop.of(shopRequestDto, image, currentUser);
        shopRepository.save(shop);
        return ShopResponseDto.from(shop);
    }


    // 가게 전체 조회
    @Transactional(readOnly = true)
    public List<ShopResponseDto> getAllShops() {
        return shopRepository.findAll().stream()
                .map(ShopResponseDto::from).collect(Collectors.toList());
    }


    // 가게 상세 조회
    @Transactional(readOnly = true)
    public ShopDetailResponseDto getShopById(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        List<ReviewResponseDto> reviews = reviewRepository.findAllByShop(shop)
                .stream()
                .map(review -> new ReviewResponseDto(
                        review.getReviewId(),
                        review.getUser().getUserId(),
                        review.getShop().getShopId(),
                        review.getUser().getNickname(),
                        review.getComment(),
                        likeRepository.countByReview(review),
                        review.getCreatedAt()))
                .collect(Collectors.toList());

        if (reviews.isEmpty()) {
            return new ShopDetailResponseDto(ShopResponseDto.from(shop), reviews, "리뷰가 아직 없습니다.");
        } else {
            return new ShopDetailResponseDto(ShopResponseDto.from(shop), reviews, "리뷰 조회 완료");
        }
    }



    // 가게 수정
    @Transactional
    public ShopResponseDto updateShop(Long shopId, ShopRequestDto shopRequestDto, MultipartFile imageUrl, @LoginAccount User currentUser) throws IOException {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        if (!shop.getUser().equals(currentUser)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        String image = s3Upload.upload(imageUrl, SHOP_BUCKET);
        shop.update(shopRequestDto, image);
        return ShopResponseDto.from(shop);
    }

    // 가게 삭제
    @Transactional
    public void deleteShop(Long shopId, @LoginAccount User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다. shopId=" + shopId));

        if (!shop.getUser().equals(currentUser)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        shopRepository.delete(shop);
    }
}
