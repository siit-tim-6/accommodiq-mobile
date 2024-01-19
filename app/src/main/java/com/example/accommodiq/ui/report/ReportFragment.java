package com.example.accommodiq.ui.report;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.JwtUtils;

public class ReportFragment extends Fragment {

    private ReportViewModel mViewModel;
    private Long accountId;
    private boolean shouldNavigateBack = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("accountId")) {
            accountId = args.getLong("accountId");
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (shouldNavigateBack) {
            Toast.makeText(getContext(), "Invalid arguments", Toast.LENGTH_SHORT).show();
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        }
    }

}