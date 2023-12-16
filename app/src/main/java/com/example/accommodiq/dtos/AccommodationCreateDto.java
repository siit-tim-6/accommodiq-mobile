package com.example.accommodiq.dtos;

import java.util.List;

public class AccommodationCreateDto {
    private String title;
    private String description;
    private String location;
    private int minGuests;
    private int maxGuests;
    private List<AvailabilityDto> available;
    private String pricingType;
    private boolean automaticAcceptance;
    private List<String> images;
    private String type;
    private List<String> benefits;

    public AccommodationCreateDto() {}

    public AccommodationCreateDto(String title, String description, String location, int minGuests, int maxGuests,
                                  List<AvailabilityDto> available, String pricingType, boolean automaticAcceptance,
                                  List<String> images, String type, List<String> benefits) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.available = available;
        this.pricingType = pricingType;
        this.automaticAcceptance = automaticAcceptance;
        this.images = images;
        this.type = type;
        this.benefits = benefits;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

     public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

     public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

     public List<AvailabilityDto> getAvailable() {
        return available;
    }

    public void setAvailable(List<AvailabilityDto> available) {
        this.available = available;
    }

     public String getPricingType() {
        return pricingType;
    }

    public void setPricingType(String pricingType) {
        this.pricingType = pricingType;
    }

     public boolean getAutomaticAcceptance() {
        return automaticAcceptance;
    }

    public void setAutomaticAcceptance(boolean automaticAcceptance) {
        this.automaticAcceptance = automaticAcceptance;
    }

     public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

     public String getType() {
        return type;
    }

    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }

     public List<String> getBenefits() {
        return benefits;
    }

    public void setType(String type) {
        this.type = type;
    }
}
