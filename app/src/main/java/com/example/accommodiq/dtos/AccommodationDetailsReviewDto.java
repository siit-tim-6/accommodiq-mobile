package com.example.accommodiq.dtos;

public class AccommodationDetailsReviewDto {
    private String author;
    private String comment;
    private double rating;
    private Long date;

    public AccommodationDetailsReviewDto(String author, String comment, double rating, Long date) {
        this.author = author;
        this.comment = comment;
        this.rating = rating;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}

