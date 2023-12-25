package com.example.accommodiq.ui.hostAccommodations;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.databinding.FragmentSearchBinding;
import com.example.accommodiq.dtos.AccommodationReviewDto;
import com.example.accommodiq.fragments.AccommodationsListFragment;
import com.example.accommodiq.fragments.FragmentTransition;
import com.example.accommodiq.models.Accommodation;
import com.example.accommodiq.services.interfaces.AccommodationApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostAccommodationsFragment extends Fragment {
    private ArrayList<Accommodation> accommodations = new ArrayList<>();
    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        prepareAccommodationList();
        Activity mainActivity = getActivity();
        if (mainActivity != null) {
            FragmentTransition.to(AccommodationsListFragment.newInstance(accommodations, false, true), getActivity(), false, R.id.accommodations_fragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareAccommodationList() {
        AccommodationApiService accommodationApiService = RetrofitClientInstance.getRetrofitInstance(getContext()).create(AccommodationApiService.class);
        Call<List<AccommodationReviewDto>> call = accommodationApiService.getHostAccommodations();
        call.enqueue(new Callback<List<AccommodationReviewDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<AccommodationReviewDto>> call, @NonNull Response<List<AccommodationReviewDto>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    fillAccommodationList(response.body());

                    if (getActivity() != null) {
                        FragmentTransition.to(AccommodationsListFragment.newInstance(accommodations, false, true), getActivity(), false, R.id.accommodations_fragment);
                    }
                } else {
                    Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AccommodationReviewDto>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillAccommodationList(List<AccommodationReviewDto> accommodationsToAdd) {
        this.accommodations.clear();
        accommodationsToAdd.forEach(accommodation -> this.accommodations.add(new Accommodation(accommodation)));
        Log.d("Accommodations", this.accommodations.toString());
    }
}
