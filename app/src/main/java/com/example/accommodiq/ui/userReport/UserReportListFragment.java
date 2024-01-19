package com.example.accommodiq.ui.userReport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.UserReportsListAdapter;

import java.util.ArrayList;

public class UserReportListFragment extends ListFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UserReportsListAdapter adapter = new UserReportsListAdapter(getContext(), new ArrayList<>());
        setListAdapter(adapter);
        return inflater.inflate(R.layout.fragment_user_report_list, container, false);
    }

}
