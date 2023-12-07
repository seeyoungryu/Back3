package com.example.withdogandcat.domain.shop;

import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.shop.dto.ShopResponseDto;
import com.example.withdogandcat.domain.shop.entity.ShopType;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.dto.ApiResponseDto;
import com.example.withdogandcat.global.tool.LoginAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shops")
public class ShopController {

    private final ShopService shopService;

    // TODO RequestParam 줄일 수 있는 방법 찾기

    // 마이페이지 가게 조회
    @GetMapping("/mypage/{userId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ApiResponseDto<List<ShopResponseDto>>> getShopsByUserId(@PathVariable Long userId) {
        ApiResponseDto<List<ShopResponseDto>> response = shopService.getShopsByUserId(userId);
        return ResponseEntity.ok(response);
    }


    // 가게 등록
    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Void> createShop(
            @RequestParam("shopName") String shopName,
            @RequestParam("shopTime") String shopTime,
            @RequestParam("shopTel") String shopTel,
            @RequestParam("shopAddress") String shopAddress,
            @RequestParam("shopType") ShopType shopType,
            @RequestParam("shopDescribe") String shopDescribe,
            @RequestParam("imageUrl") MultipartFile image,
            @LoginAccount User currentUser) throws IOException {

        ShopRequestDto shopRequestDto = new ShopRequestDto(
                shopName, shopTime, shopTel, shopAddress, shopType, shopDescribe, image);

        shopService.createShop(shopRequestDto, image, currentUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 전체 가게 조회
    @GetMapping("")
    public ResponseEntity<List<ShopResponseDto>> getAllShops() {
        List<ShopResponseDto> responseBody = shopService.getAllShops();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    // 가게 상세 조회
    @GetMapping("/{shopId}")
    public ResponseEntity<ShopResponseDto> getShopDetails(@PathVariable Long shopId) {
        ShopResponseDto shopResponseDto = shopService.getShopDetails(shopId);
        return ResponseEntity.status(HttpStatus.OK).body(shopResponseDto);
    }

    // 특정 가게 수정
    @PutMapping("/{shopId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Void> updateShop(
            @PathVariable Long shopId,
            @RequestParam("shopName") String shopName,
            @RequestParam("shopTime") String shopTime,
            @RequestParam("shopTel") String shopTel,
            @RequestParam("shopAddress") String shopAddress,
            @RequestParam("shopType") ShopType shopType,
            @RequestParam("shopDescribe") String shopDescribe,
            @RequestParam(value = "imageUrl", required = false) MultipartFile image) throws IOException {

        ShopRequestDto shopRequestDto = new ShopRequestDto(
                shopName, shopTime, shopTel, shopAddress, shopType, shopDescribe, image);

        shopService.updateShop(shopId, shopRequestDto, image);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
