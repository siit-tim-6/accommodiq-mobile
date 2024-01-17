package com.example.accommodiq.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.databinding.FragmentSearchBinding;
import com.example.accommodiq.enums.AccommodationListType;
import com.example.accommodiq.fragments.AccommodationsListFragment;
import com.example.accommodiq.fragments.FragmentTransition;

import java.util.Objects;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSearchBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Activity mainActivity = getActivity();
        if (mainActivity != null) {
            if (Objects.equals(JwtUtils.getRole(mainActivity), "ADMIN")) {
                getLayoutInflater().inflate(R.layout.fragment_user_report_list, binding.getRoot(), true); // attach to root maybe false
                return;
            }
            FragmentTransition.to(AccommodationsListFragment.newInstance(getActivity(), AccommodationListType.SEARCH), getActivity(), true, R.id.accommodations_fragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}