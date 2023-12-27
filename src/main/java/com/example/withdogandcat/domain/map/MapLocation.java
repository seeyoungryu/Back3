package com.example.withdogandcat.domain.map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MapLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 장소 이름
    private double latitude; // 위도
    private double longitude; // 경도
    private String description; // 장소에 대한 추가적인 설명

    // 기본 생성자
    public MapLocation() {
    }

    // 매개변수가 있는 생성자
    public MapLocation(String name, double latitude, double longitude, String description) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    // getter와 setter 메서드
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
