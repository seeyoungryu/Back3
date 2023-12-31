package com.example.withdogandcat.domain.hashtag.shoptag;

import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.global.common.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "shop_tag_map")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopTagMap extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_tag_map_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_tag_id")
    private ShopTag shopTag;

    @Builder
    public ShopTagMap(Shop shop, ShopTag shopTag) {
        this.shop = shop;
        this.shopTag = shopTag;
    }

}
