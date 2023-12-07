package com.example.withdogandcat.domain.shop.dto;

import com.example.withdogandcat.domain.shop.entity.ShopType;
import com.example.withdogandcat.global.config.s3.validation.FileExtension;
import com.example.withdogandcat.global.config.s3.validation.FileSize;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopRequestDto {

    @NotBlank(message = "가게명을 입력해주세요")
    private String shopName;

    private String shopTime;
    private String shopTel;
    private String shopAddress;
    private ShopType shopType;
    private String shopDescribe;

    @FileSize(max = 1048576, message = "이미지 파일은 1MB 이하이어야 합니다")
    @FileExtension(ext = "png,jpg,jpeg", message = "이미지 파일은 png, jpg, jpeg 형식이어야 합니다")
    private MultipartFile imageUrl;

    public ShopRequestDto(String shopName, String shopTime, String shopTel,
                          String shopAddress, ShopType shopType, String shopDescribe, MultipartFile imageUrl) {
        this.shopName = shopName;
        this.shopTime = shopTime;
        this.shopTel = shopTel;
        this.shopAddress = shopAddress;
        this.shopType = shopType;
        this.shopDescribe = shopDescribe;
        this.imageUrl = imageUrl;
    }
}
