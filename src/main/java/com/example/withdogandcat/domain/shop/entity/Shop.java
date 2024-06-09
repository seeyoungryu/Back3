package com.example.withdogandcat.domain.shop.entity;

import com.example.withdogandcat.domain.Image.Image;
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
@NoArgsConstructor(access = AccessLevel.PUBLIC) // 접근 수준을 PUBLIC으로 변경
public class Shop {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    private String shopName;

    private String shopStartTime;
    private String shopEndTime;

    private String shopTel1;
    private String shopTel2;
    private String shopTel3;

    private String shopAddress;
    private String shopDescribe;

    private Double latitude;
    private Double longitude;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ShopType shopType;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Image> images = new ArrayList<>();

    @Builder
    public Shop(String shopName, String shopStartTime, String shopEndTime, String shopTel1,
                String shopTel2, String shopTel3, String shopAddress, ShopType shopType,
                String shopDescribe, Double latitude, Double longitude, User user, List<Image> images) {
        this.shopName = shopName;
        this.shopStartTime = shopStartTime;
        this.shopEndTime = shopEndTime;
        this.shopTel1 = shopTel1;
        this.shopTel2 = shopTel2;
        this.shopTel3 = shopTel3;
        this.shopAddress = shopAddress;
        this.shopType = shopType;
        this.shopDescribe = shopDescribe;
        this.user = user;
        this.images.addAll(images);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Shop of(ShopRequestDto shopRequestDto, User user) {
        return Shop.builder()
                .shopName(shopRequestDto.getShopName())
                .shopStartTime(shopRequestDto.getShopStartTime())
                .shopEndTime(shopRequestDto.getShopEndTime())
                .shopTel1(shopRequestDto.getShopTel1())
                .shopTel2(shopRequestDto.getShopTel2())
                .shopTel3(shopRequestDto.getShopTel3())
                .shopAddress(shopRequestDto.getShopAddress())
                .shopType(shopRequestDto.getShopType())
                .shopDescribe(shopRequestDto.getShopDescribe())
                .latitude(shopRequestDto.getLatitude())
                .longitude(shopRequestDto.getLongitude())
                .user(user)
                .images(new ArrayList<>())
                .build();
    }

    public void updateShopDetails(String shopName, String shopStartTime, String shopEndTime,
                                  String shopTel1, String shopTel2, String shopTel3,
                                  ShopType shopType, String shopAddress,
                                  String shopDescribe, Double latitude, Double longitude) {
        this.shopName = shopName;
        this.shopStartTime = shopStartTime;
        this.shopEndTime = shopEndTime;
        this.shopTel1 = shopTel1;
        this.shopTel2 = shopTel2;
        this.shopTel3 = shopTel3;
        this.shopAddress = shopAddress;
        this.shopType = shopType;
        this.shopDescribe = shopDescribe;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void addImage(Image image) {
        images.add(image);
        image.setShop(this);
    }

    public void clearImages() {
        this.images.clear();
    }

    // 추가된 setter 메서드
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
