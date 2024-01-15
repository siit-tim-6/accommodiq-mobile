package com.example.accommodiq.ui.hostAccommodations;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.accommodiq.R;
import com.example.accommodiq.databinding.FragmentHostAccommodationsBinding;
import com.example.accommodiq.enums.AccommodationListType;
import com.example.accommodiq.fragments.AccommodationsListFragment;
import com.example.accommodiq.fragments.FragmentTransition;

public class HostAccommodationsFragment extends Fragment {
    private FragmentHostAccommodationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHostAccommodationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Activity mainActivity = getActivity();
        if (mainActivity != null) {
            FragmentTransition.to(AccommodationsListFragment.newInstance(getActivity(), AccommodationListType.HOST_ACCOMMODATIONS), getActivity(), true, R.id.host_accommodations_fragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
