package com.example.withdogandcat.domain.shop;

import com.example.withdogandcat.domain.Image.Image;
import com.example.withdogandcat.domain.Image.ImageS3Service;
import com.example.withdogandcat.domain.hashtag.shoptag.ShopTag;
import com.example.withdogandcat.domain.hashtag.shoptag.ShopTagMap;
import com.example.withdogandcat.domain.hashtag.shoptag.ShopTagMapRepository;
import com.example.withdogandcat.domain.hashtag.shoptag.ShopTagRepository;
import com.example.withdogandcat.domain.review.ReviewRepository;
import com.example.withdogandcat.domain.review.dto.ReviewResponseDto;
import com.example.withdogandcat.domain.shop.dto.ShopDetailResponseDto;
import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.shop.dto.ShopResponseDto;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.shop.entity.ShopType;
import com.example.withdogandcat.domain.shop.repo.ShopRepository;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseException;
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
    private final ImageS3Service imageS3Service;
    private final ReviewRepository reviewRepository;
    private final ShopTagRepository shopTagRepository;
    private final ShopTagMapRepository shopTagMapRepository;

    private static final int MAX_SHOPS_PER_USER = 5;

    /**
     * 가게 등록
     */
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
        int reviewCount = reviewRepository.countByShop(shop);
        return ShopResponseDto.from(shop, reviewCount);
    }

    /**
     * 가게 전체 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<ShopResponseDto>> getAllShops() {
        List<ShopResponseDto> shops = shopRepository.findAll().stream()
                .map(shop -> {
                    int reviewCount = reviewRepository.countByShop(shop);
                    return ShopResponseDto.from(shop, reviewCount);
                }).collect(Collectors.toList());

        if (shops.isEmpty()) {
            return new BaseResponse<>(BaseResponseStatus.SHOP_NOT_FOUND);
        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", shops);
    }

    /**
     * 가게 상세 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse<ShopDetailResponseDto> getShopDetails(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        int reviewCount = reviewRepository.countByShop(shop);
        ShopResponseDto shopResponse = ShopResponseDto.from(shop, reviewCount);

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

        ShopDetailResponseDto detailResponse = ShopDetailResponseDto.builder()
                .shopResponseDto(shopResponse)
                .reviews(reviews)
                .build();

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", detailResponse);
    }

    /**
     * 가게 수정
     */
    @Transactional
    public BaseResponse<ShopResponseDto> updateShop(Long shopId, ShopRequestDto shopRequestDto,
                                                    List<MultipartFile> imageFiles, User currentUser) throws IOException {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        if (!shop.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new BaseException(BaseResponseStatus.ACCESS_DENIED);
        }

        if (imageFiles != null && !imageFiles.isEmpty() && !imageFiles.stream().allMatch(MultipartFile::isEmpty)) {
            imageS3Service.deleteImages(shop.getImages());
            shop.clearImages();
            List<Image> newImages = imageS3Service.uploadMultipleImagesForShop(imageFiles, shop);
            newImages.forEach(shop::addImage);
        }

        shop.updateShopDetails(
                shopRequestDto.getShopName(),
                shopRequestDto.getShopStartTime(),
                shopRequestDto.getShopEndTime(),
                shopRequestDto.getShopTel1(),
                shopRequestDto.getShopTel2(),
                shopRequestDto.getShopTel3(),
                shopRequestDto.getShopType(),
                shopRequestDto.getShopAddress(),
                shopRequestDto.getShopDescribe(),
                shopRequestDto.getLatitude(),
                shopRequestDto.getLongitude()
        );
        Shop updatedShop = shopRepository.save(shop);
        int reviewCount = reviewRepository.countByShop(updatedShop);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", ShopResponseDto.from(updatedShop, reviewCount));
    }

    /**
     * 가게 삭제
     */
    @Transactional
    public void deleteShop(Long shopId, User currentUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        if (!shop.getUser().equals(currentUser)) {
            throw new BaseException(BaseResponseStatus.ACCESS_DENIED);
        }

        List<ShopTagMap> relatedTags = shopTagMapRepository.findByShop(shop);

        for (ShopTagMap tagMap : relatedTags) {
            ShopTag tag = tagMap.getShopTag();
            shopTagMapRepository.delete(tagMap);

            if (shopTagMapRepository.countByShopTag(tag) == 0) {
                shopTagRepository.delete(tag);
            }
        }

        imageS3Service.deleteImages(shop.getImages());
        reviewRepository.deleteByShop(shop);
        shopRepository.delete(shop);
    }


    /**
     * 카테고리 별 가게 조회
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<ShopResponseDto>> getShopsByCategory(ShopType shopType) {
        List<Shop> shops = shopRepository.findAllByShopType(shopType);
        if (shops.isEmpty()) {
            return new BaseResponse<>(BaseResponseStatus.SHOP_NOT_FOUND);
        }

        List<ShopResponseDto> shopDtos = shops.stream()
                .map(shop -> {
                    int reviewCount = reviewRepository.countByShop(shop);
                    return ShopResponseDto.from(shop, reviewCount);
                })
                .collect(Collectors.toList());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", shopDtos);
    }

    /**
     * 가게 검색
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<ShopResponseDto>> searchShops(String keyword) {
        List<Shop> searchResults = shopRepository.searchShops(keyword);
        if (searchResults.isEmpty()) {
            return new BaseResponse<>(BaseResponseStatus.SHOP_NOT_FOUND);
        }

        List<ShopResponseDto> shopDtos = searchResults.stream()
                .map(shop -> {
                    int reviewCount = reviewRepository.countByShop(shop);
                    return ShopResponseDto.from(shop, reviewCount);
                })
                .collect(Collectors.toList());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", shopDtos);
    }


    /**
     * 주어진 이름과 주소에 해당하는 Shop을 찾거나, 없으면 새로 생성합니다.
     * @param shopName 상점 이름
     * @param geoLocation GeoLocation 객체 (위도, 경도, 주소)
     * @return Shop 객체
     */
    public Shop findOrCreateShop(String shopName, GeoLocation geoLocation) {
        Shop shop = shopRepository.findByShopNameAndAddress(shopName, geoLocation.getAddress());
        if (shop == null) {
            shop = new Shop();
            shop.setShopName(shopName);
            shop.setShopAddress(geoLocation.getAddress());
            shop.setLatitude(geoLocation.getLatitude());
            shop.setLongitude(geoLocation.getLongitude());
            shopRepository.save(shop);
        }
        return shop;
    }

}
