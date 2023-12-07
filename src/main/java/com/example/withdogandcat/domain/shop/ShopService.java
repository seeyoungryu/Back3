package com.example.withdogandcat.domain.shop;

import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.shop.dto.ShopResponseDto;
import com.example.withdogandcat.domain.shop.entitiy.Shop;
import com.example.withdogandcat.domain.shop.ShopRepository;
import com.example.withdogandcat.global.config.s3.S3Upload;
import com.example.withdogandcat.global.exception.CustomException;
import com.example.withdogandcat.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final S3Upload s3Upload;

    private static final String SHOP_BUCKET = "shop-pet"; // ShopService에서 사용할 버킷 이름

    // 가게 등록
    @Transactional
    public ShopResponseDto addShop(ShopRequestDto shopRequestDto, MultipartFile imageUrl) throws IOException {
        String image = uploadImageToS3(imageUrl);

        Shop shop = Shop.of(shopRequestDto, image);
        shopRepository.save(shop);
        return ShopResponseDto.from(shop);
    }

    // 이미지 등록 메서드
    private String uploadImageToS3(MultipartFile imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new CustomException(ErrorCode.ELEMENTS_IS_REQUIRED);
        }
        return s3Upload.upload(imageUrl, SHOP_BUCKET);
    }
}
