package com.example.accommodiq.dtos;

public class AccommodationStatusDto {
    private String status;

    public AccommodationStatusDto() {
    }

    public AccommodationStatusDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
