package com.example.withdogandcat.domain.shop.dto;

import com.example.withdogandcat.domain.shop.entity.ShopType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopRequestDto {

    @NotBlank(message = "가게명을 입력해주세요")
    @Size(min = 1, max = 20, message = "최소 1자 이상, 최대 20자 이하로 작성해주세요.")
    private String shopName;

    @NotBlank(message = "영업 시작 시간을 입력해주세요")
    private String shopStartTime;

    @NotBlank(message = "영업 종료 시간을 입력해주세요")
    private String shopEndTime;

    @NotBlank(message = "전화번호 첫 부분을 입력해주세요")
    private String shopTel1;

    @NotBlank(message = "전화번호 중간 부분을 입력해주세요")
    private String shopTel2;

    @NotBlank(message = "전화번호 마지막 부분을 입력해주세요")
    private String shopTel3;

    @NotBlank(message = "주소를 입력해주세요")
    @Size(min = 1, max = 30, message = "최소 1자 이상, 최대 30자 이하로 작성해주세요.")
    private String shopAddress;

    private ShopType shopType;

    private Double latitude;
    private Double longitude;

    @NotBlank(message = "가게 설명을 입력해주세요")
    @Size(min = 1, max = 100, message = "최소 1자 이상, 최대 100자 이하로 작성해주세요.")
    private String shopDescribe;

    private List<MultipartFile> imageFiles;

    public ShopRequestDto(String shopName, String shopStartTime, String shopEndTime,
                          String shopTel1, String shopTel2, String shopTel3,
                          String shopAddress, ShopType shopType, String shopDescribe,
                          List<MultipartFile> imageFiles, Double latitude, Double longitude) {
        this.shopName = shopName;
        this.shopStartTime = shopStartTime;
        this.shopEndTime = shopEndTime;
        this.shopTel1 = shopTel1;
        this.shopTel2 = shopTel2;
        this.shopTel3 = shopTel3;
        this.shopAddress = shopAddress;
        this.shopType = shopType;
        this.shopDescribe = shopDescribe;
        this.imageFiles = imageFiles;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
