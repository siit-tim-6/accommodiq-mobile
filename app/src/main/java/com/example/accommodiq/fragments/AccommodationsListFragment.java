package com.example.accommodiq.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.ListFragment;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.AccommodationListAdapter;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.AccommodationClient;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.models.Accommodation;
import com.example.accommodiq.utils.DateUtils;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class AccommodationsListFragment extends ListFragment {
    private AccommodationListAdapter adapter;
    private AccommodationClient accommodationClient;
    private List<AccommodationListDto> accommodations;
    private Long dateFrom = null;
    private Long dateTo = null;

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
        searchAccommodations(null, null, null, null, null, null, null, null, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText titleSearch = view.findViewById(R.id.title_search);
        EditText guestsSearch = view.findViewById(R.id.guests_search);
        EditText locationSearch = view.findViewById(R.id.location_search);
        TextView dateRangeSearch = view.findViewById(R.id.date_range_search);
        Button moreFiltersBtn = view.findViewById(R.id.more_filters);
        Button searchBtn = view.findViewById(R.id.accommodations_search);
        Button clearBtn = view.findViewById(R.id.clear_search);
        CalendarConstraints dateValidator = (new CalendarConstraints.Builder()).setValidator(DateValidatorPointForward.now()).build();

        MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setCalendarConstraints(dateValidator)
                .setTheme(R.style.ThemeMaterialCalendar)
                .setTitleText("Select check-in and check-out date").setSelection(new Pair<>(null, null)).build();

        dateRangeSearch.setOnClickListener(v -> {
            if (!dateRangePicker.isAdded())
                dateRangePicker.show(this.getParentFragmentManager(), "AccommodIQ");

            dateRangePicker.addOnNegativeButtonClickListener(v1 -> {
                dateRangePicker.dismiss();
            });

            dateRangePicker.addOnPositiveButtonClickListener(selection -> {
                dateFrom = selection.first / 1000;
                dateTo = selection.second / 1000;
                dateRangeSearch.setText(String.format("%s - %s", DateUtils.convertTimeToDate(selection.first), DateUtils.convertTimeToDate(selection.second)));
            });
        });

        searchBtn.setOnClickListener(v -> {
            String titleSearchText = !titleSearch.getText().toString().isEmpty() ? titleSearch.getText().toString() : null;
            Integer guestsSearchNumber = !guestsSearch.getText().toString().isEmpty() ? Integer.valueOf(guestsSearch.getText().toString()) : null;
            String locationSearchText = !locationSearch.getText().toString().isEmpty() ? locationSearch.getText().toString() : null;

            searchAccommodations(titleSearchText, locationSearchText, dateFrom, dateTo, null, null, guestsSearchNumber, null, null);
        });

        clearBtn.setOnClickListener(v -> {
            titleSearch.setText("");
            guestsSearch.setText("");
            locationSearch.setText("");
            dateFrom = null;
            dateTo = null;
            dateRangeSearch.setText(R.string.date_range_hint);

            searchAccommodations(null, null, null, null, null, null, null, null, null);
        });
    }

    private void searchAccommodations(String title, String location, Long availableFrom, Long availableTo,
                                      Integer priceFrom, Integer priceTo, Integer guests, String type, String benefits) {
        Call<List<AccommodationListDto>> accommodationsCall = accommodationClient.getAccommodations(title, location, availableFrom, availableTo, priceFrom,
                priceTo, guests, type, benefits);
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
