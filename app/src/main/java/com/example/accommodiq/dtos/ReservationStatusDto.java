package com.example.accommodiq.dtos;

public class ReservationStatusDto {
    private String status;

    public ReservationStatusDto() {}

    public ReservationStatusDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
