package com.example.accommodiq.dtos;

import com.example.accommodiq.models.Accommodation;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

public class AccommodationDetailsDto {
    private Long id;
    private String title;
    private double rating;
    private int reviewCount;
    private String location;
    private AccommodationDetailsHostDto host;
    private List<String> images;
    private int minGuests;
    private int maxGuests;
    private String description;
    private List<AccommodationDetailsReviewDto> reviews;
    private Set<String> benefits;

    private String type;

    public AccommodationDetailsDto(Long id, String title, double rating, int reviewCount, String location, AccommodationDetailsHostDto host, List<String> images,
                                   int minGuests, int maxGuests, String description, ArrayList<AccommodationDetailsReviewDto> reviews, Set<String> benefits, String type) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.location = location;
        this.host = host;
        this.images = images;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.description = description;
        this.reviews = reviews;
        this.benefits = benefits;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public AccommodationDetailsHostDto getHost() {
        return host;
    }

    public void setHost(AccommodationDetailsHostDto host) {
        this.host = host;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AccommodationDetailsReviewDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<AccommodationDetailsReviewDto> reviews) {
        this.reviews = reviews;
    }

    public Set<String> getBenefits() {
        return benefits;
    }

    public void setBenefits(Set<String> benefits) {
        this.benefits = benefits;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}