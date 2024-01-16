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

import java.util.ArrayList;

public class AccommodationReviewApprovalListFragment extends ListFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setListAdapter(new AccommodationReviewApprovalsAdapter(getContext(), new ArrayList<>()));
        return inflater.inflate(R.layout.fragment_accommodation_review_approval_list, container, false);
    }
}
