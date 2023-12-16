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

}
