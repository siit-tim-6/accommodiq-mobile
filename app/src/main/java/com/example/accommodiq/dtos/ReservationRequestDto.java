package com.example.accommodiq.dtos;

public class ReservationRequestDto {
    private long startDate;
    private long endDate;
    private int numberOfGuests;
    private long accommodationId;

    public ReservationRequestDto() { }

    public ReservationRequestDto(long startDate, long endDate, int numberOfGuests, long accommodationId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.accommodationId = accommodationId;
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

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(long accommodationId) {
        this.accommodationId = accommodationId;
    }
}
