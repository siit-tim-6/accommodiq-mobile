package com.example.accommodiq.ui.updateAccommodationAvailability;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.AvailabilityRangeListAdapter;
import com.example.accommodiq.models.Availability;

import java.util.List;

public class UpdateAccommodationAvailability extends Fragment {

    private UpdateAccommodationAvailabilityViewModel mViewModel;
    private RecyclerView recyclerView;
    private AvailabilityRangeListAdapter adapter;
    private List<Availability> availabilityList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_accommodation_availability, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Assume availabilityList is initialized and contains data
        adapter = new AvailabilityRangeListAdapter(availabilityList, getContext());
        recyclerView.setAdapter(adapter);

        // Set up click listener or other mechanisms for updating availability ranges

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(UpdateAccommodationAvailabilityViewModel.class);
    }
    // Additional methods for handling updates

}