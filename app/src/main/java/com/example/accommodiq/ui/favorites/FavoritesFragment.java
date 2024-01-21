package com.example.accommodiq.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

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

public class FavoritesFragment extends ListFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccommodationClient client = RetrofitClientInstance.getRetrofitInstance(requireContext()).create(AccommodationClient.class);
        Call<List<AccommodationListDto>> call = client.getFavorites();
        call.enqueue(new Callback<List<AccommodationListDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<AccommodationListDto>> call, @NonNull Response<List<AccommodationListDto>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Error happened", Toast.LENGTH_SHORT).show();
                    return;
                }
                assert response.body() != null;
                List<AccommodationListDto> accommodations = response.body();
                AccommodationListAdapter adapter = new AccommodationListAdapter(getActivity(), (ArrayList<AccommodationListDto>) accommodations, AccommodationListType.FAVORITES);
                setListAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<AccommodationListDto>> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error happened", Toast.LENGTH_SHORT).show();
            }
        });

        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }
}