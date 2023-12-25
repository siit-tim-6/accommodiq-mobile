package com.example.accommodiq.dtos;

public class AccommodationDetailsHostDto {
    private long id;
    private String name;
    private double rating;
    private int reviewCount;

    public AccommodationDetailsHostDto() { }

    public AccommodationDetailsHostDto(long id, String name, double rating, int reviewCount) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}