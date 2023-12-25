package com.example.accommodiq.dtos;

import com.example.accommodiq.models.Availability;

import java.util.List;

public class AccommodationBookingDetailFormDto {
    private int cancellationDeadline;
    private String pricingType;
    private List<Availability> available;

    public AccommodationBookingDetailFormDto() {
    }

    public AccommodationBookingDetailFormDto(int cancellationDeadline, String pricingType, List<Availability> available) {
        this.cancellationDeadline = cancellationDeadline;
        this.pricingType = pricingType;
        this.available = available;
    }

    public int getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(int cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public String getPricingType() {
        return pricingType;
    }

    public void setPricingType(String pricingType) {
        this.pricingType = pricingType;
    }

    public List<Availability> getAvailable() {
        return available;
    }

    public void setAvailable(List<Availability> available) {
        this.available = available;
    }
}
