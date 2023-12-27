package com.example.withdogandcat.domain.map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {

    private static final Logger logger = LoggerFactory.getLogger(MapController.class);

    private final GeoLocationService geoLocationService;
    private List<Place> savedResults; // 저장된 검색 결과를 임시로 저장하는 리스트

    @Autowired
    public MapController(GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
        this.savedResults = new ArrayList<>();
    }

    @GetMapping("")
    public ResponseEntity<List<Place>> getSavedResults() {

        return ResponseEntity.ok(savedResults);
    }

    @PostMapping("")
    public ResponseEntity<?> saveLocation(@RequestBody List<Place> places) {
        try {
            savedResults.addAll(places);
            logger.info("검색 결과를 저장했습니다.");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("검색 결과 저장 실패:", e);
            return ResponseEntity.badRequest().build();
        }
    }
}