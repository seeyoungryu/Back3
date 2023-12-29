package com.example.withdogandcat.domain.map;

import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MapShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private double latitude;
    private double longitude;

    @OneToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Builder
    public MapShop(String address, double latitude, double longitude, Shop shop) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.shop = shop;
    }

    public void updateLocation(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}