package com.example.accommodiq.dtos;

import com.example.accommodiq.clients.ReservationClient;

public class ReservationCardDto {
    private long id;
    private long accommodationId;
    private String accommodationImage;
    private String accommodationTitle;
    private double accommodationRating;
    private int accommodationReviewCount;
    private LocationDto accommodationLocation;
    private int guests;
    private long startDate;
    private long endDate;
    private String status;
    private double totalPrice;

    public ReservationCardDto() {}

    public ReservationCardDto(long id, long accommodationId, String accommodationImage, String accommodationTitle, double accommodationRating, int accommodationReviewCount, LocationDto accommodationLocation, int guests, long startDate, long endDate, String status, double totalPrice) {
        this.id = id;
        this.accommodationId = accommodationId;
        this.accommodationImage = accommodationImage;
        this.accommodationTitle = accommodationTitle;
        this.accommodationRating = accommodationRating;
        this.accommodationReviewCount = accommodationReviewCount;
        this.accommodationLocation = accommodationLocation;
        this.guests = guests;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getAccommodationImage() {
        return accommodationImage;
    }

    public void setAccommodationImage(String accommodationImage) {
        this.accommodationImage = accommodationImage;
    }

    public String getAccommodationTitle() {
        return accommodationTitle;
    }

    public void setAccommodationTitle(String accommodationTitle) {
        this.accommodationTitle = accommodationTitle;
    }

    public double getAccommodationRating() {
        return accommodationRating;
    }

    public void setAccommodationRating(double accommodationRating) {
        this.accommodationRating = accommodationRating;
    }

    public int getAccommodationReviewCount() {
        return accommodationReviewCount;
    }

    public void setAccommodationReviewCount(int accommodationReviewCount) {
        this.accommodationReviewCount = accommodationReviewCount;
    }

    public LocationDto getAccommodationLocation() {
        return accommodationLocation;
    }

    public void setAccommodationLocation(LocationDto accommodationLocation) {
        this.accommodationLocation = accommodationLocation;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
