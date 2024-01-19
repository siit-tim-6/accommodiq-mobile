package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.ReviewStatus;

public class ReviewDto {
    private long id;
    private int rating;
    private String comment;
    private Long date;
    private ReviewStatus status;
    private Long authorId;
    private String author;
    private boolean deletable;

    public ReviewDto() {
    }

    public ReviewDto(long id, int rating, String comment, Long date, ReviewStatus status, Long authorId, String author, boolean deletable) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.status = status;
        this.authorId = authorId;
        this.author = author;
        this.deletable = deletable;
    }

    public long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Long getDate() {
        return date;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }
}
