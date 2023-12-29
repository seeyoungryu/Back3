package com.example.withdogandcat.domain.shop;

import com.example.mailtest.domain.Image.Image;
import com.example.mailtest.domain.Image.ImageS3Service;
import com.example.mailtest.domain.review.ReviewRepository;
import com.example.mailtest.domain.review.dto.ReviewResponseDto;
import com.example.mailtest.domain.shop.dto.ShopDetailResponseDto;
import com.example.mailtest.domain.shop.dto.ShopRequestDto;
import com.example.mailtest.domain.shop.dto.ShopResponseDto;
import com.example.mailtest.domain.shop.entity.Shop;
import com.example.mailtest.domain.shop.entity.ShopType;
import com.example.mailtest.domain.user.entity.User;
import com.example.mailtest.global.common.BaseResponse;
import com.example.mailtest.global.exception.BaseException;
import com.example.mailtest.global.exception.BaseResponseStatus;
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
    private final ImageS3Service imageS3Service;

    private static final int MAX_SHOPS_PER_USER = 5;

    // 마이페이지 가게 조회
    @Transactional(readOnly = true)
    public BaseResponse<List<ShopResponseDto>> getShopsByCurrentUser(User currentUser) {
        List<Shop> shops = shopRepository.findByUser(currentUser);
        if (shops.isEmpty()) {
            return new BaseResponse<>(BaseResponseStatus.SHOP_NOT_FOUND);
        }

        List<ShopResponseDto> shopDtos = shops.stream()
                .map(ShopResponseDto::from).collect(Collectors.toList());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", shopDtos);
    }

    // 가게 등록
    @Transactional
    public ShopResponseDto createShop(ShopRequestDto shopRequestDto,
                                      List<MultipartFile> imageFiles, User user) throws IOException {

        int currentShopCount = shopRepository.countByUser(user);
        if (currentShopCount >= MAX_SHOPS_PER_USER) {
            throw new BaseException(BaseResponseStatus.EXCEED_MAX_SHOP_LIMIT);
        }

        Shop shop = Shop.of(shopRequestDto, user);
        List<Image> uploadedImages = imageS3Service.uploadMultipleImagesForShop(imageFiles, shop);
        uploadedImages.forEach(shop::addImage);
        shopRepository.save(shop);
        return ShopResponseDto.from(shop);
    }

    // 가게 전체 조회
    @Transactional(readOnly = true)
    public BaseResponse<List<ShopResponseDto>> getAllShops() {
        List<ShopResponseDto> shops = shopRepository.findAll().stream()
                .map(ShopResponseDto::from).collect(Collectors.toList());

        if (shops.isEmpty()) {
            return new BaseResponse<>(BaseResponseStatus.SHOP_NOT_FOUND);
        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", shops);
    }

    // 가게 상세 조회
    @Transactional(readOnly = true)
    public BaseResponse<ShopDetailResponseDto> getShopDetails(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        List<ReviewResponseDto> reviews = reviewRepository.findByShopId(shopId).stream()
                .map(review -> ReviewResponseDto.builder()
                        .reviewId(review.getReviewId())
                        .userId(review.getUser().getUserId())
                        .shopId(review.getShop().getShopId())
                        .nickname(review.getUser().getNickname())
                        .comment(review.getComment())
                        .likeCount(review.getLikeCount())
                        .createdAt(review.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        ShopResponseDto shopResponse = ShopResponseDto.from(shop);
        ShopDetailResponseDto detailResponse = ShopDetailResponseDto.builder()
                .shopResponseDto(shopResponse)
                .reviews(reviews)
                .build();

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", detailResponse);
    }

    // 가게 수정
    @Transactional
    public BaseResponse<ShopResponseDto> updateShop(Long shopId, ShopRequestDto shopRequestDto,
                                                    List<MultipartFile> imageFiles, User currentUser) throws IOException {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        if (!shop.getUser().getUserId().equals(currentUser.getUserId())) {
            return new BaseResponse<>(BaseResponseStatus.USER_NOT_FOUND, "유저를 찾을 수 없습니다.", null);
        }

        if (imageFiles != null && !imageFiles.isEmpty() && !imageFiles.stream().allMatch(MultipartFile::isEmpty)) {
            imageS3Service.deleteImages(shop.getImages());
            shop.clearImages();
            List<Image> newImages = imageS3Service.uploadMultipleImagesForShop(imageFiles, shop);
            newImages.forEach(shop::addImage);
        }

        shop.updateShopDetails(
                shopRequestDto.getShopName(),
                shopRequestDto.getShopTime(),
                shopRequestDto.getShopTel(),
                shopRequestDto.getShopType(),
                shopRequestDto.getShopAddress(),
                shopRequestDto.getShopDescribe());
        Shop updatedShop = shopRepository.save(shop);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", ShopResponseDto.from(updatedShop));
    }

    // 가게 삭제
    @Transactional
    public BaseResponse<Void> deleteShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        imageS3Service.deleteImages(shop.getImages());
        reviewRepository.deleteByShop(shop);
        shopRepository.delete(shop);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null);
    }

    // 카테고리별 가게 조회
    @Transactional(readOnly = true)
    public BaseResponse<List<ShopResponseDto>> getShopsByCategory(ShopType shopType) {
        List<Shop> shops = shopRepository.findAllByShopType(shopType);
        if (shops.isEmpty()) {
            return new BaseResponse<>(BaseResponseStatus.SHOP_NOT_FOUND);
        }

        List<ShopResponseDto> shopDtos = shops.stream()
                .map(ShopResponseDto::from).collect(Collectors.toList());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", shopDtos);
    }

}
