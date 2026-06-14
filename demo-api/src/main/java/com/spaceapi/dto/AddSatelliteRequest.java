package com.spaceapi.dto;

public class AddSatelliteRequest {

    private Long satelliteId;

    public AddSatelliteRequest() {
    }

    public AddSatelliteRequest(Long satelliteId) {
        this.satelliteId = satelliteId;
    }

    public Long getSatelliteId() {
        return satelliteId;
    }

    public void setSatelliteId(Long satelliteId) {
        this.satelliteId = satelliteId;
    }
}
