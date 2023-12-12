package com.example.withdogandcat.domain.shop;

import com.example.withdogandcat.domain.image.Image;
import com.example.withdogandcat.domain.image.ImageS3Service;
import com.example.withdogandcat.domain.review.dto.ReviewResponseDto;
import com.example.withdogandcat.domain.like.LikeRepository;
import com.example.withdogandcat.domain.shop.dto.ShopDetailResponseDto;
import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.shop.dto.ShopResponseDto;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.shop.entity.ShopType;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.tool.ApiResponseDto;
import com.example.withdogandcat.global.exception.CustomException;
import com.example.withdogandcat.global.exception.ErrorCode;
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
    private final ReviewRepository reviewRepository;
    private final ImageS3Service imageS3Service;

    // 마이페이지 가게 조회
    public ApiResponseDto<List<ShopResponseDto>> getShopsByCurrentUser(User currentUser) {
        List<Shop> shops = shopRepository.findByUser(currentUser);
        String message = shops.isEmpty() ? "등록된 가게가 없습니다" : "가게 목록 조회 성공";
        List<ShopResponseDto> shopDtos = shops.stream()
                .map(ShopResponseDto::from).collect(Collectors.toList());
        return new ApiResponseDto<>(message, shopDtos);
    }

    // 가게 등록
    @Transactional
    public ShopResponseDto createShop(ShopRequestDto shopRequestDto, List<MultipartFile> imageFiles, User user) throws IOException {
        Shop shop = Shop.of(shopRequestDto, user);
        List<Image> uploadedImages = imageS3Service.uploadMultipleImages(imageFiles, shop);
        uploadedImages.forEach(shop::addImage);
        shopRepository.save(shop);
        return ShopResponseDto.from(shop);
    }

    // 가게 전체 조회
    @Transactional(readOnly = true)
    public ApiResponseDto<List<ShopResponseDto>> getAllShops() {
        List<ShopResponseDto> shops = shopRepository.findAll().stream()
                .map(ShopResponseDto::from).collect(Collectors.toList());

        String message = shops.isEmpty() ? "등록된 가게가 없습니다" : "가게 전체 조회 성공";
        return new ApiResponseDto<>(message, shops);
    }

    // 가게 상세 조회
    @Transactional(readOnly = true)
    public ShopDetailResponseDto getShopDetails(Long shopId) {

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new CustomException(ErrorCode.SHOP_NOT_FOUND));

        List<ReviewResponseDto> reviews = reviewRepository.findByShopId(shopId).stream()
                .map(review -> ReviewResponseDto.builder()
                        .reviewId(review.getReviewId())
                        .userId(review.getUser().getUserId())
                        .shopId(review.getShop().getShopId())
                        .nickname(review.getUser().getNickname())
                        .comment(review.getComment())
                        .likeCount(review.getLikeCount())
                        .createdAt(review.getCreatedAt())
                        .build()).collect(Collectors.toList());

        ShopResponseDto shopResponse = ShopResponseDto.from(shop);

        return ShopDetailResponseDto.builder().shopResponseDto(shopResponse).reviews(reviews).build();
    }

    // 가게 수정
    @Transactional
    public ShopResponseDto updateShop(Long shopId, ShopRequestDto shopRequestDto,
                                      List<MultipartFile> imageFiles, User currentUser) throws IOException {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new CustomException(ErrorCode.SHOP_NOT_FOUND));
        if (!shop.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new CustomException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        if (imageFiles != null && !imageFiles.isEmpty() && imageFiles.stream().anyMatch(file -> !file.isEmpty())) {
            imageS3Service.deleteImages(shop.getImages());
            shop.clearImages();
            List<Image> newImages = imageS3Service.uploadMultipleImages(imageFiles, shop);
            newImages.forEach(shop::addImage);
        }

        shop.updateShopDetails(
                shopRequestDto.getShopName(),
                shopRequestDto.getShopTime(),
                shopRequestDto.getShopTel(),
                shopRequestDto.getShopType(),
                shopRequestDto.getShopAddress(),
                shopRequestDto.getShopDescribe());
        return ShopResponseDto.from(shopRepository.save(shop));
    }

    // 가게 삭제
    @Transactional
    public void deleteShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new CustomException(ErrorCode.SHOP_NOT_FOUND));

        imageS3Service.deleteImages(shop.getImages());
        reviewRepository.deleteByShop(shop);
        shopRepository.delete(shop);
    }

    // 카테고리별 가게 조회
    @Transactional(readOnly = true)
    public List<ShopResponseDto> getShopsByCategory(ShopType shopType) {
        List<Shop> shops = shopRepository.findAllByShopType(shopType);

        if (shops.isEmpty()) {
            throw new CustomException(ErrorCode.SHOP_NOT_FOUND);
        }

        return shops.stream()
                .map(ShopResponseDto::from)
                .collect(Collectors.toList());
    }
}
