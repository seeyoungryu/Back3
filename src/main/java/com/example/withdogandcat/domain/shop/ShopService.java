package com.example.withdogandcat.domain.shop;

import com.example.withdogandcat.domain.image.Image;
import com.example.withdogandcat.domain.image.ImageS3Service;
import com.example.withdogandcat.domain.review.ReviewRepository;
import com.example.withdogandcat.domain.review.dto.ReviewResponseDto;
import com.example.withdogandcat.domain.shop.dto.ShopDetailResponseDto;
import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.shop.dto.ShopResponseDto;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.shop.entity.ShopType;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
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


    @Transactional
    public ShopResponseDto createShop(ShopRequestDto shopRequestDto, List<MultipartFile> imageFiles, User user) throws IOException {
        Shop shop = Shop.of(shopRequestDto, user);
        List<Image> uploadedImages = imageS3Service.uploadMultipleImages(imageFiles, shop);
        uploadedImages.forEach(shop::addImage);
        shopRepository.save(shop);
        return ShopResponseDto.from(shop);
    }

    @Transactional(readOnly = true)
    public BaseResponse<List<ShopResponseDto>> getAllShops() {
        List<ShopResponseDto> shops = shopRepository.findAll().stream()
                .map(ShopResponseDto::from).collect(Collectors.toList());

        if (shops.isEmpty()) {
            return new BaseResponse<>(BaseResponseStatus.SHOP_NOT_FOUND);
        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", shops);
    }

    @Transactional(readOnly = true)
    public BaseResponse<ShopDetailResponseDto> getShopDetails(Long shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow();

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

    @Transactional
    public BaseResponse<ShopResponseDto> updateShop(Long shopId, ShopRequestDto shopRequestDto,
                                                    List<MultipartFile> imageFiles, User currentUser) throws IOException {
        Shop shop = shopRepository.findById(shopId).orElseThrow();

        if (!shop.getUser().getUserId().equals(currentUser.getUserId())) {
            return new BaseResponse<>(BaseResponseStatus.USER_NOT_FOUND, "유저를 찾을 수 없습니다.", null);
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
        Shop updatedShop = shopRepository.save(shop);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", ShopResponseDto.from(updatedShop));
    }


    @Transactional
    public BaseResponse<Void> deleteShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow();

        imageS3Service.deleteImages(shop.getImages());
        reviewRepository.deleteByShop(shop);
        shopRepository.delete(shop);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null);
    }

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
