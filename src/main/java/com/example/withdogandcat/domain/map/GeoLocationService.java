package com.example.withdogandcat.domain.map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GeoLocationService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private static final String API_URL = "https://dapi.kakao.com/v2/local/search/address.json";

    /**
     * 주소를 받아 카카오 API를 통해 위도와 경도로 변환
     * @param address 변환할 주소
     * @return GeoLocation 객체 (위도, 경도, 주소)
     */
    public GeoLocation getGeoLocationFromAddress(String address) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "?query=" + address))
                    .header("Authorization", "KakaoAK " + kakaoApiKey)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = new JSONObject(response.body());
            JSONObject location = jsonResponse.getJSONArray("documents").getJSONObject(0);

            double latitude = location.getJSONObject("road_address").getDouble("y");
            double longitude = location.getJSONObject("road_address").getDouble("x");

            return new GeoLocation(latitude, longitude, address);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class GeoLocation {
        private final double latitude;
        private final double longitude;
        private final String address;

        public GeoLocation(double latitude, double longitude, String address) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.address = address;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getAddress() {
            return address;
        }
    }
}
