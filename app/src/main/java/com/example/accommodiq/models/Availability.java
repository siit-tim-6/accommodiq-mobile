package com.example.accommodiq.models;

public class Availability {
    private Long id;
    private Long fromDate;
    private Long toDate;
    private double price;

    public Availability(Long id,Long fromDate,Long toDate,double price) {
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }
}
