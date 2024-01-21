package com.example.accommodiq.dtos;

public class FinancialReportMonthlyEntryDto {
    private String month;
    private double revenue;
    private int reservationCount;

    public FinancialReportMonthlyEntryDto(String month, double revenue, int reservationCount) {
        this.month = month;
        this.revenue = revenue;
        this.reservationCount = reservationCount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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
}
