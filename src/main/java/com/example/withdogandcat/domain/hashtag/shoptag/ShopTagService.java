package com.example.withdogandcat.domain.hashtag.shoptag;

import com.example.withdogandcat.domain.shop.ShopRepository;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopTagService {

    private final ShopRepository shopRepository;
    private final ShopTagRepository shopTagRepository;
    private final ShopTagMapRepository shopTagMapRepository;

    private final int MAX_TAGS_PER_SHOP = 3;

    @Transactional
    public ShopTagDto addTagToShop(Long shopId, String tagName, Long userId) {
        if (tagName == null || tagName.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.ELEMENTS_IS_REQUIRED);
        }

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        if (!shop.getUser().getUserId().equals(userId)) {
            throw new BaseException(BaseResponseStatus.ACCESS_DENIED);
        }

        ShopTag shopTag = shopTagRepository.findByName(tagName)
                .orElseGet(() -> shopTagRepository.save(new ShopTag(null, tagName)));

        if (shopTagMapRepository.findByShopAndShopTag(shop, shopTag).isPresent()) {
            throw new BaseException(BaseResponseStatus.ALREADY_EXISTS);
        }

        long currentTagCount = shopTagMapRepository.countByShop(shop);
        if (currentTagCount >= MAX_TAGS_PER_SHOP) {
            throw new BaseException(BaseResponseStatus.EXCEED_MAX_TAG_LIMIT);
        }

        ShopTagMap shopTagMap = ShopTagMap.builder().shop(shop).shopTag(shopTag).build();
        shopTagMapRepository.save(shopTagMap);

        return new ShopTagDto(shopTag.getId(), shopTag.getName());
    }

    @Transactional
    public void removeTagFromShop(Long shopId, String tagName, Long userId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        ShopTag shopTag = shopTagRepository.findByName(tagName)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.TAG_NOT_FOUND));

        ShopTagMap shopTagMap = shopTagMapRepository.findByShopAndShopTag(shop, shopTag)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_TAG_NOT_FOUND));

        if (!shop.getUser().getUserId().equals(userId)) {
            throw new BaseException(BaseResponseStatus.ACCESS_DENIED);
        }

        shopTagMapRepository.delete(shopTagMap);

        long count = shopTagMapRepository.countByShopTag(shopTag);
        if (count == 0) {
            shopTagRepository.delete(shopTag);
        }
    }

    @Transactional(readOnly = true)
    public List<ShopTagDto> getShopsByTag(String tagName) {
        ShopTag shopTag = shopTagRepository.findByName(tagName)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.TAG_NOT_FOUND));

        return shopTagMapRepository.findByShopTag(shopTag).stream()
                .map(shopTagMap -> new ShopTagDto(shopTagMap.getShopTag().getId(), shopTagMap.getShopTag().getName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ShopTagDto> getPopularShopTags(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> tagFrequencies = shopTagMapRepository.findShopTagUsageFrequency(pageable);
        return tagFrequencies.stream()
                .map(obj -> new ShopTagDto((Long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ShopTagDto> getAllTags() {
        List<ShopTag> allTags = shopTagRepository.findAll();
        return allTags.stream()
                .map(ShopTagDto::from)
                .collect(Collectors.toList());
    }

}
