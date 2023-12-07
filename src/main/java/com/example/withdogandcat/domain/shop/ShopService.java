package com.example.withdogandcat.domain.shop;

import com.example.withdogandcat.domain.review.ReviewRepository;
import com.example.withdogandcat.domain.review.dto.ReviewResponseDto;
import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.shop.dto.ShopResponseDto;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.config.s3.S3Upload;
import com.example.withdogandcat.global.dto.ApiResponseDto;
import com.example.withdogandcat.global.exception.CustomException;
import com.example.withdogandcat.global.exception.ErrorCode;
import com.example.withdogandcat.global.tool.LoginAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final ReviewRepository reviewRepository;
    private final S3Upload s3Upload;

    private static final String SHOP_BUCKET = "shop-pet"; // ShopService에서 사용할 버킷 이름

    // 가게 등록
    @Transactional
    public ShopResponseDto createShop(ShopRequestDto shopRequestDto, MultipartFile imageUrl, @LoginAccount User currentUser) throws IOException {
        String image = s3Upload.upload(imageUrl, SHOP_BUCKET);
        Shop shop = Shop.of(shopRequestDto, image, currentUser);
        shopRepository.save(shop);
        return ShopResponseDto.from(shop);
    }

    // 가게 상세 조회
    @Transactional(readOnly = true)
    public ShopResponseDto getShopDetails(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new CustomException(ErrorCode.SHOP_NOT_FOUND));

        List<ReviewResponseDto> reviews = reviewRepository.findByShop_ShopId(shopId).stream()
                .map(review -> ReviewResponseDto.builder()
                        .reviewId(review.getReviewId())
                        .userId(review.getUser().getUserId())
                        .nickname(review.getUser().getNickname())
                        .comment(review.getComment())
                        .likeCount(review.getLikeCount())
                        .createdAt(review.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        if (reviews.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_YET_COMMENT);
        }

        return ShopResponseDto.from(shop, reviews);
    }

    // 가게 전체 조회
    @Transactional(readOnly = true)
    public List<ShopResponseDto> getAllShops() {
        return shopRepository.findAll().stream()
                .map(ShopResponseDto::from).collect(Collectors.toList());
    }

    // 가게 수정
    @Transactional
    public ShopResponseDto updateShop(Long shopId,
                                      ShopRequestDto shopRequestDto,
                                      MultipartFile imageUrl) throws IOException {

        Shop shop = shopRepository.findById(shopId).orElseThrow(
                () -> new CustomException(ErrorCode.SHOP_NOT_FOUND));

        String image = s3Upload.upload(imageUrl, SHOP_BUCKET);

        shop.updateShopDetails(
                shopRequestDto.getShopName(),
                shopRequestDto.getShopTime(),
                shopRequestDto.getShopTel(),
                shopRequestDto.getShopType(),
                shopRequestDto.getShopAddress(),
                shopRequestDto.getShopDescribe(),
                image);

        return ShopResponseDto.from(shop);
    }

    // 마이페이지 가게 조회 TODO 유저아이디 반환해야 함
    public ApiResponseDto<List<ShopResponseDto>> getShopsByUserId(Long userId) {
        List<Shop> shops = shopRepository.findByUser_UserId(userId);

        String message = shops.isEmpty() ? "등록된 가게가 없습니다" : "가게 목록 조회 성공";
        List<ShopResponseDto> shopDtos = shops.stream()
                .map(ShopResponseDto::from)
                .collect(Collectors.toList());

        return new ApiResponseDto<>(message, shopDtos);
    }
}
