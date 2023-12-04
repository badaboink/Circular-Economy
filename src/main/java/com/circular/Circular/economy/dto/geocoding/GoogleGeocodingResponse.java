package com.circular.Circular.economy.dto.geocoding;

import java.util.List;

public class GoogleGeocodingResponse {
    private String status;
    private List<GeocodingResult> results;

    // getters and setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GeocodingResult> getResults() {
        return results;
    }

    public void setResults(List<GeocodingResult> results) {
        this.results = results;
    }
}
