package com.example.accommodiq.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.AccommodationListAdapter;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.AccommodationClient;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.models.Accommodation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationsListFragment extends ListFragment {
    private AccommodationListAdapter adapter;
    private AccommodationClient accommodationClient;
    private List<AccommodationListDto> accommodations;

    public static AccommodationsListFragment newInstance(Context context) {
        AccommodationsListFragment fragment = new AccommodationsListFragment();
        fragment.accommodationClient = RetrofitClientInstance.getRetrofitInstance(context).create(AccommodationClient.class);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceBundle) {
        return inflater.inflate(R.layout.fragment_accommodations_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Call<List<AccommodationListDto>> accommodationsCall = accommodationClient.getAccommodations(null, null, null, null, null,
                null, null, null, null);
        accommodationsCall.enqueue(new Callback<List<AccommodationListDto>>() {
            @Override
            public void onResponse(Call<List<AccommodationListDto>> call, Response<List<AccommodationListDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    accommodations = (ArrayList<AccommodationListDto>) response.body();
                    adapter = new AccommodationListAdapter(getActivity(), (ArrayList<AccommodationListDto>) accommodations);
                    setListAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationListDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}
