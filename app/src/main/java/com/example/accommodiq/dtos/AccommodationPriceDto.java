package com.example.accommodiq.dtos;

import com.example.accommodiq.models.Accommodation;

public class AccommodationPriceDto {
    private double totalPrice;

    public AccommodationPriceDto() { }

    public AccommodationPriceDto(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
