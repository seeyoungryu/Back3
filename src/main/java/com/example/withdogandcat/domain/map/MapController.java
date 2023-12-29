package com.example.withdogandcat.domain.map;

import com.example.withdogandcat.domain.map.dto.MapShopRequestDto;
import com.example.withdogandcat.domain.map.dto.MapShopResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/shops")
public class MapController {

    private final MapService mapService;

    @Autowired
    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @PostMapping("/map")
    public ResponseEntity<MapShopResponseDto> addLocation(@RequestBody MapShopRequestDto mapShopRequestDto) {
        MapShopResponseDto mapShopResponseDto = mapService.addLocation(mapShopRequestDto.getShopId(), mapShopRequestDto);
        return ResponseEntity.ok(mapShopResponseDto);
    }

    @GetMapping("/{shopId}/map")
    public ResponseEntity<MapShopResponseDto> getLocation(@PathVariable Long shopId) {
        MapShopResponseDto mapShopResponseDto = mapService.getLocation(shopId);
        return ResponseEntity.ok(mapShopResponseDto);
    }

    @PutMapping("/{shopId}/map")
    public ResponseEntity<MapShopResponseDto> updateLocation(@PathVariable Long shopId, @RequestBody MapShopRequestDto mapShopRequestDto) {
        MapShopResponseDto mapShopResponseDto = mapService.updateLocation(shopId, mapShopRequestDto);
        return ResponseEntity.ok(mapShopResponseDto);
    }
}