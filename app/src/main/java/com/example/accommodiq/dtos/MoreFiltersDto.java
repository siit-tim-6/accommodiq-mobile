package com.example.accommodiq.dtos;

public class MoreFiltersDto {
    private Integer minPrice;
    private Integer maxPrice;
    private String selectedType;
    private String benefits;

    public MoreFiltersDto() { }

    public MoreFiltersDto(Integer minPrice, Integer maxPrice, String selectedType, String benefits) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.selectedType = selectedType;
        this.benefits = benefits;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }
}
