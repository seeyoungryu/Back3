package com.example.withdogandcat.domain.shop.dto;

import com.example.withdogandcat.domain.shop.entity.ShopType;
import com.example.withdogandcat.domain.image.validation.FileExtension;
import com.example.withdogandcat.domain.image.validation.FileSize;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    private List<MultipartFile> imageFiles;

    public ShopRequestDto(String shopName, String shopTime, String shopTel,
                          String shopAddress, ShopType shopType, String shopDescribe,
                          List<MultipartFile> imageFiles) {
        this.shopName = shopName;
        this.shopTime = shopTime;
        this.shopTel = shopTel;
        this.shopAddress = shopAddress;
        this.shopType = shopType;
        this.shopDescribe = shopDescribe;
        this.imageFiles = imageFiles;
    }
}
