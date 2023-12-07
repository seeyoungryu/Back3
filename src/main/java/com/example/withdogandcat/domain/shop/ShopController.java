package com.example.withdogandcat.domain.shop;

import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.shop.entitiy.ShopType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shops")
public class ShopController {

    private final ShopService shopService;

    // 가게 등록
    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<String> addShop (
            @RequestParam("shopName") String shopName,
            @RequestParam("shopTime") String shopTime,
            @RequestParam("shopTel") String shopTel,
            @RequestParam("shopAddress") String shopAddress,
            @RequestParam("shopType") ShopType shopType,
            @RequestParam("shopDescribe") String shopDescribe,
            @RequestParam("imageUrl") MultipartFile image) throws IOException {

        ShopRequestDto shopRequestDto = new ShopRequestDto(
                shopName, shopTime, shopTel, shopAddress, shopType, shopDescribe, image);

        shopService.addShop(shopRequestDto, image);

        return ResponseEntity.ok().build();
    }
}

