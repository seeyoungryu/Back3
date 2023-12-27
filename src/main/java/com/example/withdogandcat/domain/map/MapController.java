//package com.example.withdogandcat.domain.map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/map")
//public class MapController {
//
//    private final GeoLocationService geoLocationService;
//
//    @Autowired
//    public MapController(GeoLocationService geoLocationService) {
//        this.geoLocationService = geoLocationService;
//    }
//
//    @GetMapping("")
//    public ResponseEntity<GeoLocationService.GeoLocation> getLocation(@RequestParam String address) {
//        GeoLocationService.GeoLocation location = geoLocationService.getGeoLocationFromAddress(address);
//        if (location == null) {
//            return ResponseEntity.badRequest().build();
//        }
//        return ResponseEntity.ok(location);
//    }
//}

package com.example.withdogandcat.domain.map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
public class MapController {

    private static final Logger logger = LoggerFactory.getLogger(MapController.class); // 로거 추가

    private final GeoLocationService geoLocationService;

    @Autowired
    public MapController(GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
    }

    @GetMapping("")
    public ResponseEntity<GeoLocationService.GeoLocation> getLocation(@RequestParam String address) {
        logger.info("받은 주소: {}", address); // 주소 정보 로깅
        GeoLocationService.GeoLocation location = geoLocationService.getGeoLocationFromAddress(address);
        if (location == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(location);
    }
}
