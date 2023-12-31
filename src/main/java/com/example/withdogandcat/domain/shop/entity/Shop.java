package com.example.withdogandcat.domain.shop.entity;

import com.example.withdogandcat.domain.Image.Image;
import com.example.withdogandcat.domain.shop.dto.ShopRequestDto;
import com.example.withdogandcat.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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

    private String shopStartTime;
    private String shopEndTime;

    private String shopTel1;
    private String shopTel2;
    private String shopTel3;

    private String shopAddress;
    private String shopDescribe;

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
                String shopDescribe, User user, List<Image> images) {
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
                .user(user)
                .images(new ArrayList<>())
                .build();
    }

    public void updateShopDetails(String shopName, String shopStartTime, String shopEndTime, String shopTel1,
                                  String shopTel2, String shopTel3, ShopType shopType, String shopAddress,
                                  String shopDescribe) {
        this.shopName = shopName;
        this.shopStartTime = shopStartTime;
        this.shopEndTime = shopEndTime;
        this.shopTel1 = shopTel1;
        this.shopTel2 = shopTel2;
        this.shopTel3 = shopTel3;
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
