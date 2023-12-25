package com.example.accommodiq.ui.accommodationReview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.accommodiq.R;
import com.example.accommodiq.activities.RegisterActivity;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.databinding.FragmentAccommodationReviewBinding;
import com.example.accommodiq.databinding.FragmentSearchBinding;
import com.example.accommodiq.dtos.AccommodationDetailsDto;
import com.example.accommodiq.dtos.AccommodationReviewDto;
import com.example.accommodiq.dtos.RegisterDto;
import com.example.accommodiq.fragments.AccommodationsListFragment;
import com.example.accommodiq.fragments.FragmentTransition;
import com.example.accommodiq.models.Accommodation;
import com.example.accommodiq.services.interfaces.AccommodationApiService;
import com.example.accommodiq.ui.search.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationReviewFragment extends Fragment {
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
            FragmentTransition.to(AccommodationsListFragment.newInstance(accommodations), getActivity(), false, R.id.accommodations_fragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareAccommodationList() {
        AccommodationApiService accommodationApiService = RetrofitClientInstance.getRetrofitInstance(getContext()).create(AccommodationApiService.class);
        Call<List<AccommodationReviewDto>> call = accommodationApiService.getPendingAccommodations();
        call.enqueue(new Callback<List<AccommodationReviewDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<AccommodationReviewDto>> call, @NonNull Response<List<AccommodationReviewDto>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                    assert response.body() != null;
                    fillAccommodationList(response.body());

                    if (getActivity() != null) {
                        FragmentTransition.to(AccommodationsListFragment.newInstance(accommodations), getActivity(), false, R.id.accommodations_fragment);
                    }
                } else {
                    Toast.makeText(getContext(), "Registration failed. Please try again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AccommodationReviewDto>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fillAccommodationList(List<AccommodationReviewDto> accommodationsToAdd) {
        this.accommodations.clear();
        accommodationsToAdd.forEach(accommodation -> this.accommodations.add(new Accommodation(accommodation)));
        Log.d("Accommodations", this.accommodations.toString());
    }
}
