package com.example.accommodiq.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import androidx.navigation.Navigation;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.AccommodationListAdapter;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.AccommodationClient;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.enums.AccommodationListType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends ListFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccommodationClient accommodationClient = RetrofitClientInstance.getRetrofitInstance(requireContext()).create(AccommodationClient.class);
        Call<List<AccommodationListDto>> call = accommodationClient.getAccommodations(null, null, null, null, null, null, null, null, null);
        call.enqueue(new Callback<List<AccommodationListDto>>() {
             @Override
             public void onResponse(@NonNull Call<List<AccommodationListDto>> call, @NonNull Response<List<AccommodationListDto>> response) {
                 if (response.isSuccessful()) {
                     List<AccommodationListDto> accommodations = response.body();
                     if (accommodations != null) {
                         AccommodationListAdapter adapter = new AccommodationListAdapter(requireContext(), (ArrayList<AccommodationListDto>) accommodations, AccommodationListType.SEARCH);
                         setListAdapter(adapter);
                     }
                 }
             }
             @Override
             public void onFailure(@NonNull Call<List<AccommodationListDto>> call, @NonNull Throwable t) {
             }
        });
        return inflater.inflate(R.layout.fragment_accommodations_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Activity mainActivity = getActivity();
        if (mainActivity != null) {
            if (Objects.equals(JwtUtils.getRole(mainActivity), "ADMIN")) {
                Navigation.findNavController(requireView()).navigate(R.id.navigation_user_reports);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}