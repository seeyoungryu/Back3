package com.example.withdogandcat.domain.Image;

import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.domain.shop.entity.Shop;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Builder
    public Image(String originName, String storedImagePath, Shop shop, Pet pet) {
        this.originName = originName;
        this.storedImagePath = storedImagePath;
        this.shop = shop;
        this.pet = pet;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
