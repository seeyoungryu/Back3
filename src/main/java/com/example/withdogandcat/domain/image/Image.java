package com.example.withdogandcat.domain.image;

import com.example.withdogandcat.domain.shop.entity.Shop;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String storedImagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Builder
    public Image(String originName, String storedImagePath, Shop shop) {
        this.originName = originName;
        this.storedImagePath = storedImagePath;
        this.shop = shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
