package com.example.withdogandcat.domain.hashtag.shoptag;

import com.example.withdogandcat.domain.hashtag.chattag.TagDtoInterface;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopTagDto implements TagDtoInterface {

    private Long id;
    private String name;

    public ShopTagDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ShopTagDto from(ShopTag shopTag) {
        return new ShopTagDto(shopTag.getId(), shopTag.getName());
    }

}
