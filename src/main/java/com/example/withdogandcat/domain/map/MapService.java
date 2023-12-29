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

    @Transactional
    public void savePlaces(List<Place> places) {
        for (Place place : places) {
            placeRepository.save(place);
        }
    }

    @Transactional(readOnly = true)
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }
}