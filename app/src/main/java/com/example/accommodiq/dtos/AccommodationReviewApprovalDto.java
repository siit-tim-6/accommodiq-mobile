package com.example.accommodiq.dtos;

public class AccommodationReviewApprovalDto {
    private long accommodationId;
    private String image;
    private String title;
    private ReviewApprovalDto review;

    public AccommodationReviewApprovalDto() {
    }

    public long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ReviewApprovalDto getReview() {
        return review;
    }

    public void setReview(ReviewApprovalDto review) {
        this.review = review;
    }
}
