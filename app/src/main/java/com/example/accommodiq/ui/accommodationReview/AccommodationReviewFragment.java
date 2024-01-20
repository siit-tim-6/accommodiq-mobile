package com.example.accommodiq.ui.accommodationReview;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import androidx.navigation.Navigation;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.AccommodationListAdapter;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.AccommodationClient;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.enums.AccommodationListType;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationReviewFragment extends ListFragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccommodationClient accommodationClient = RetrofitClientInstance.getRetrofitInstance(requireContext()).create(AccommodationClient.class);
        accommodationClient.getPendingAccommodations().enqueue(new Callback<List<AccommodationListDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<AccommodationListDto>> call, @NonNull Response<List<AccommodationListDto>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Error: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                assert response.body() != null;
                setListAdapter(new AccommodationListAdapter(requireContext(), (ArrayList<AccommodationListDto>) response.body(), AccommodationListType.ADMIN_REVIEW_PENDING_ACCOMMODATIONS));
            }

            @Override
            public void onFailure(@NonNull Call<List<AccommodationListDto>> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return inflater.inflate(R.layout.fragment_accommodation_review, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
