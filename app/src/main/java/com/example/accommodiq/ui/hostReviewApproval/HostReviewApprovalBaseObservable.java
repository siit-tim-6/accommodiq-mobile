package com.example.accommodiq.ui.hostReviewApproval;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.accommodiq.dtos.HostReviewApprovalDto;

public class HostReviewApprovalBaseObservable extends BaseObservable {
    private final HostReviewApprovalDto hostReviewApprovalDto;

    public HostReviewApprovalBaseObservable(HostReviewApprovalDto hostReviewApprovalDto) {
        this.hostReviewApprovalDto = hostReviewApprovalDto;
    }

    @Bindable
    public HostReviewApprovalDto getHostReviewApprovalDto() {
        return hostReviewApprovalDto;
    }
}
