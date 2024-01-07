package com.example.withdogandcat.domain.hashtag.shoptag;


import com.example.withdogandcat.global.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "shop_tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopTag extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public ShopTag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ShopTag from(ShopTagDto shopTagDto) {
        return ShopTag.builder()
                .id(shopTagDto.getId())
                .name(shopTagDto.getName())
                .build();
    }

}
