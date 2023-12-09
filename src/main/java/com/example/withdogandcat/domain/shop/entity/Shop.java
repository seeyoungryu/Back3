package com.example.withdogandcat.domain.shop.entity;

import com.example.withdogandcat.domain.image.Image;
import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "shops")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shop {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    private String shopName;
    private String shopTime;
    private String shopTel;
    private String shopAddress;
    private String shopDescribe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ShopType shopType;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    @Builder
    public Shop(String shopName, String shopTime, String shopTel,
                String shopAddress, ShopType shopType,
                String shopDescribe, User user, List<Image> images) {
        this.shopName = shopName;
        this.shopTime = shopTime;
        this.shopTel = shopTel;
        this.shopAddress = shopAddress;
        this.shopType = shopType;
        this.shopDescribe = shopDescribe;
        this.user = user;
        this.images.addAll(images);
    }

    public static Shop of(ShopRequestDto shopRequestDto, User user) {
        return Shop.builder()
                .shopName(shopRequestDto.getShopName())
                .shopTime(shopRequestDto.getShopTime())
                .shopTel(shopRequestDto.getShopTel())
                .shopAddress(shopRequestDto.getShopAddress())
                .shopType(shopRequestDto.getShopType())
                .shopDescribe(shopRequestDto.getShopDescribe())
                .user(user)
                .images(new ArrayList<>())
                .build();
    }

    public void updateShopDetails(String shopName, String shopTime, String shopTel,
                                  ShopType shopType, String shopAddress,
                                  String shopDescribe) {
        this.shopName = shopName;
        this.shopTime = shopTime;
        this.shopTel = shopTel;
        this.shopAddress = shopAddress;
        this.shopType = shopType;
        this.shopDescribe = shopDescribe;
    }

    public void addImage(Image image) {
        images.add(image);
        image.setShop(this);
    }

    public void clearImages() {
        this.images.clear();
    }
}
