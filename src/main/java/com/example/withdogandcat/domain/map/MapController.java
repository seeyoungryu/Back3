package com.example.withdogandcat.domain.map;

import com.example.withdogandcat.domain.shop.ShopService;
import com.example.withdogandcat.domain.shop.entity.Shop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {

    private static final Logger logger = LoggerFactory.getLogger(MapController.class);

    private final GeoLocationService geoLocationService;
    private final MapService mapService;
    private final ShopService shopService;

    @Autowired
    public MapController(GeoLocationService geoLocationService, MapService mapService, ShopService shopService) {
        this.geoLocationService = geoLocationService;
        this.mapService = mapService;
        this.shopService = shopService;
    }

    /**
     * 저장된 Place 목록을 반환
     * @return List of Place
     */
    @GetMapping("")
    public ResponseEntity<List<Place>> getSavedResults() {
        List<Place> places = mapService.getAllPlaces();
        return ResponseEntity.ok(places);
    }

    /**
     * 주어진 PlaceRequest 목록을 기반으로 Place를 저장
     * @param placeRequests PlaceRequest 목록
     * @return ResponseEntity
     */
    @PostMapping("")
    public ResponseEntity<?> saveLocation(@RequestBody List<PlaceRequest> placeRequests) {
        try {
            for (PlaceRequest placeRequest : placeRequests) {
                GeoLocationService.GeoLocation geoLocation = geoLocationService.getGeoLocationFromAddress(placeRequest.getAddress());
                Shop shop = shopService.findOrCreateShop(placeRequest.getShopName(), geoLocation);
                Place place = new Place(geoLocation.getAddress(), geoLocation.getLatitude(), geoLocation.getLongitude(), shop);
                mapService.savePlace(place);
            }
            logger.info("검색 결과를 저장했습니다.");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("검색 결과 저장 실패:", e);
            return ResponseEntity.badRequest().build();
        }
    }
}

class PlaceRequest {
    private String address;
    private String shopName;

    // getters and setters
}
