package com.example.accommodiq.ui.accommodationReviewApproval;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.accommodiq.dtos.AccommodationReviewApprovalDto;

public class AccommodationReviewApprovalBaseObservable extends BaseObservable {
    private final AccommodationReviewApprovalDto accommodationReviewApprovalDto;

    public AccommodationReviewApprovalBaseObservable(AccommodationReviewApprovalDto accommodationReviewApprovalDto) {
        this.accommodationReviewApprovalDto = accommodationReviewApprovalDto;
    }

    @Bindable
    public AccommodationReviewApprovalDto getAccommodationReviewApprovalDto() {
        return accommodationReviewApprovalDto;
    }

    @Bindable
    public String getDismissButtonText() {
        return accommodationReviewApprovalDto.getReview().getStatus().equals("PENDING") ? "Decline" : "Delete";
    }
}
