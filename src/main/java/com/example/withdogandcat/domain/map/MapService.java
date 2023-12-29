package com.example.withdogandcat.domain.map;

import com.example.withdogandcat.domain.map.dto.MapShopRequestDto;
import com.example.withdogandcat.domain.map.dto.MapShopResponseDto;
import com.example.withdogandcat.domain.shop.ShopRepository;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MapService {

    private final MapShopRepository mapShopRepository;
    private final ShopRepository shopRepository;

    @Autowired
    public MapService(MapShopRepository mapShopRepository, ShopRepository shopRepository) {
        this.mapShopRepository = mapShopRepository;
        this.shopRepository = shopRepository;
    }

    @Transactional
    public MapShopResponseDto addLocation(Long shopId, MapShopRequestDto mapShopRequestDto) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        MapShop mapShop = MapShop.builder()
                .address(mapShopRequestDto.getAddress())
                .latitude(mapShopRequestDto.getLatitude())
                .longitude(mapShopRequestDto.getLongitude())
                .build();

        mapShopRepository.save(mapShop);

        return new MapShopResponseDto(mapShop);
    }

    @Transactional(readOnly = true)
    public MapShopResponseDto getLocation(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        MapShop mapShop = mapShopRepository.findByShop(shop)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.MAP_LOCATION_NOT_FOUND));

        return new MapShopResponseDto(mapShop);
    }


    @Transactional
    public MapShopResponseDto updateLocation(Long shopId, MapShopRequestDto requestDto) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.SHOP_NOT_FOUND));

        MapShop mapShop = mapShopRepository.findByShop(shop)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.MAP_LOCATION_NOT_FOUND));

        mapShop.updateLocation(requestDto.getAddress(), requestDto.getLatitude(), requestDto.getLongitude());
        mapShopRepository.save(mapShop);

        return new MapShopResponseDto(mapShop);
    }

}