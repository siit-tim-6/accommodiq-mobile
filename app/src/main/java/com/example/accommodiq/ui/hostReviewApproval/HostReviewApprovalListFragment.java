package com.example.accommodiq.ui.hostReviewApproval;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.HostReviewApprovalsAdapter;

import java.util.ArrayList;

public class HostReviewApprovalListFragment extends ListFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HostReviewApprovalsAdapter adapter = new HostReviewApprovalsAdapter(getContext(), new ArrayList<>());
        setListAdapter(adapter);
        return inflater.inflate(R.layout.fragment_host_review_approval_list, container, false);
    }
}
