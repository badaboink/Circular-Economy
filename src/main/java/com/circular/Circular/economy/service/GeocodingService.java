package com.circular.Circular.economy.service;

import com.circular.Circular.economy.dto.geocoding.Coordinates;
import com.circular.Circular.economy.dto.geocoding.Geometry;
import com.circular.Circular.economy.dto.geocoding.GoogleGeocodingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeocodingService {

    @Value("AIzaSyDXBoMxUb1A-6yy3bSWPXE1QHPnwD6jmI4")
    private String apiKey;

    private static final String GEOCODING_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    public Coordinates getCoordinatesFromAddress(String address) {
        String url = String.format("%s?address=%s&key=%s", GEOCODING_API_URL, address, apiKey);
        RestTemplate restTemplate = new RestTemplate();
        GoogleGeocodingResponse response = restTemplate.getForObject(url, GoogleGeocodingResponse.class);

        if (response != null && "OK".equals(response.getStatus())) {
            Geometry geometry = response.getResults().get(0).getGeometry();
            return new Coordinates(geometry.getLocation().getLat(), geometry.getLocation().getLng());
        }

        return null;
    }
}
