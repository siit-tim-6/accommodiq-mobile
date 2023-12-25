package com.example.accommodiq.dtos;

public class AccommodationBookingDetailsDto {
    private int cancellationDeadline;
    private String pricingType;

    public AccommodationBookingDetailsDto() {
    }

    public AccommodationBookingDetailsDto(int cancellationDeadline, String pricingType) {
        this.cancellationDeadline = cancellationDeadline;
        this.pricingType = pricingType;
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
}
