package com.example.accommodiq.dtos;

public class FinancialReportEntryDto {
    private String accommodationTitle;
    private double revenue;
    private int reservationCount;
    private String color;

    public FinancialReportEntryDto(String accommodationTitle, double revenue, int reservationCount, String color) {
        this.accommodationTitle = accommodationTitle;
        this.revenue = revenue;
        this.reservationCount = reservationCount;
        this.color = color;
    }

    public String getAccommodationTitle() {
        return accommodationTitle;
    }

    public void setAccommodationTitle(String accommodationTitle) {
        this.accommodationTitle = accommodationTitle;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public int getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(int reservationCount) {
        this.reservationCount = reservationCount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
