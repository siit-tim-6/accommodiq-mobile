package com.example.accommodiq.ui.newAccommodation;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.AvailabilityRangeListAdapter;
import com.example.accommodiq.databinding.FragmentNewAccommodationBinding;
import com.example.accommodiq.models.Availability;

public class NewAccommodation extends Fragment {

    private FragmentNewAccommodationBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        NewAccommodationViewModel newAccommodationViewModel = new ViewModelProvider(this).get(NewAccommodationViewModel.class);

        binding = FragmentNewAccommodationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up RecyclerView with the adapter
        AvailabilityRangeListAdapter adapter = new AvailabilityRangeListAdapter([]);
        binding.recyclerViewPriceRange.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}