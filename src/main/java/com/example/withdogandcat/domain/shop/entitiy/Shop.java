package com.example.withdogandcat.domain.shop.entitiy;

import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "shop")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    @Column(nullable = false, unique = true)
    private String shopName;

    @Column(nullable = false)
    private String shopTime;

    @Column(nullable = false)
    private String shopTel;

    @Column(nullable = false)
    private String shopAddress;

    @Column(nullable = false)
    private String shopDescribe;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShopType shopType;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Shop(String shopName, String shopTime, String shopTel,
                 String shopAddress, ShopType shopType,
                 String shopDescribe, String imageUrl)
    {
        this.shopName = shopName;
        this.shopTime = shopTime;
        this.shopTel = shopTel;
        this.shopAddress = shopAddress;
        this.shopType = shopType;
        this.shopDescribe = shopDescribe;
        this.imageUrl = imageUrl;
    }

    public static Shop of(ShopRequestDto shopRequestDto, String imageUrl) {
        return Shop.builder()
                .shopName(shopRequestDto.getShopName())
                .shopTime(shopRequestDto.getShopTime())
                .shopTel(shopRequestDto.getShopTel())
                .shopAddress(shopRequestDto.getShopAddress())
                .shopType(shopRequestDto.getShopType())
                .shopDescribe(shopRequestDto.getShopDescribe())
                .imageUrl(imageUrl)
                .build();
    }
}
