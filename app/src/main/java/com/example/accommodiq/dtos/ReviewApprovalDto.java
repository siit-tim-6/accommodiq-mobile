package com.example.accommodiq.dtos;

public class ReviewApprovalDto {
    long id;
    double rating;
    String comment;
    long date;
    String status;
    long authorId;
    String author;
    public ReviewApprovalDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
