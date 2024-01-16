package com.example.accommodiq.ui.accommodationReviewApproval;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.AccommodationReviewApprovalsAdapter;
import com.example.accommodiq.databinding.FragmentAccommodationReviewApprovalListBinding;

import java.util.ArrayList;

public class AccommodationReviewApprovalListFragment extends ListFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AccommodationReviewApprovalsAdapter adapter = new AccommodationReviewApprovalsAdapter(getContext(), new ArrayList<>());
        setListAdapter(adapter);

        View view = inflater.inflate(R.layout.fragment_accommodation_review_approval_list, container, false);
        FragmentAccommodationReviewApprovalListBinding binding = FragmentAccommodationReviewApprovalListBinding.bind(view);
        binding.toggleButtonShowReportedReviews.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                adapter.showReviewsByType("REPORTED");
            } else {
                adapter.showReviewsByType("PENDING");
            }
        });
        return binding.getRoot();
    }
}
