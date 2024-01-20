package com.example.accommodiq.dtos;

public class HostReviewApprovalDto {
    private ReviewApprovalDto review;
    private String name;
    private Long hostId;

    public HostReviewApprovalDto() {
    }

    public ReviewApprovalDto getReview() {
        return review;
    }

    public void setReview(ReviewApprovalDto review) {
        this.review = review;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }
}
