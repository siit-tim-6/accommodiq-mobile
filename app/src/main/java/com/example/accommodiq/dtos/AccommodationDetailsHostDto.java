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
}
