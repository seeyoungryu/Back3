package com.example.withdogandcat.domain.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MapService {

    private final PlaceRepository placeRepository;

    @Autowired
    public MapService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    /**
     * Place를 저장합니다.
     * @param place 저장할 Place
     */
    @Transactional
    public void savePlace(Place place) {
        placeRepository.save(place);
    }

    /**
     * 모든 Place를 반환합니다.
     * @return List of Place
     */
    @Transactional(readOnly = true)
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }
}
