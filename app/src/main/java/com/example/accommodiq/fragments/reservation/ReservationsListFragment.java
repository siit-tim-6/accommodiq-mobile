package com.example.accommodiq.fragments.reservation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.versionedparcelable.ParcelImpl;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.ReservationListAdapter;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.ReservationClient;
import com.example.accommodiq.dtos.ReservationCardDto;
import com.example.accommodiq.enums.ReservationListType;
import com.example.accommodiq.utils.DateUtils;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationsListFragment extends ListFragment {
    private ReservationListAdapter adapter;
    private ReservationClient reservationClient;
    private List<ReservationCardDto> reservations = null;
    private List<Long> cancellableReservationIds = null;
    private ReservationListType type;
    private Long dateFrom = null;
    private Long dateTo = null;

    public ReservationsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reservations_list, container, false);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reservationClient = RetrofitClientInstance.getRetrofitInstance(getContext()).create(ReservationClient.class);

        String role = JwtUtils.getRole(getContext());
        if (Objects.equals(role, "GUEST")) {
            type = ReservationListType.GUEST;
        } else {
            type = ReservationListType.HOST;
        }

        if (type == ReservationListType.GUEST) {
            searchReservations("guests", null, null, null, null);
            refreshCancellableIds();
        } else {
            searchReservations("hosts", null, null, null, null);
        }
    }

    private void searchReservations(String roleString, String title, Long startDate, Long endDate, String status) {
        Call<List<ReservationCardDto>> reservationsCall = reservationClient.getReservations(roleString, title, startDate, endDate, status);

        reservationsCall.enqueue(new Callback<List<ReservationCardDto>>() {
            @Override
            public void onResponse(Call<List<ReservationCardDto>> call, Response<List<ReservationCardDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reservations = response.body();

                    if (type == ReservationListType.GUEST && reservations != null && cancellableReservationIds != null) {
                        adapter = new ReservationListAdapter(getActivity(), (ArrayList<ReservationCardDto>) reservations, type, (ArrayList<Long>) cancellableReservationIds);
                        setListAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReservationCardDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshCancellableIds() {
        Call<List<Long>> reservationsCall = reservationClient.getCancellableReservationIds();

        reservationsCall.enqueue(new Callback<List<Long>>() {
            @Override
            public void onResponse(Call<List<Long>> call, Response<List<Long>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cancellableReservationIds = response.body();

                    if (type == ReservationListType.GUEST && reservations != null && cancellableReservationIds != null) {
                        adapter = new ReservationListAdapter(getActivity(), (ArrayList<ReservationCardDto>) reservations, type, (ArrayList<Long>) cancellableReservationIds);
                        setListAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Long>> call, Throwable t) {
                Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText titleSearch = view.findViewById(R.id.reservation_title_search);
        TextView dateRangeSearch = view.findViewById(R.id.reservation_date_range_search);
        Spinner statusSearch = view.findViewById(R.id.reservation_status_search);
        Button searchBtn = view.findViewById(R.id.reservation_search);
        Button clearBtn = view.findViewById(R.id.reservation_clear_search);

        MaterialDatePicker<androidx.core.util.Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.ThemeMaterialCalendar)
                .setTitleText("Select reservation date range").setSelection(new Pair<>(null, null)).build();

        dateRangeSearch.setOnClickListener(v -> {
            if (!dateRangePicker.isAdded())
                dateRangePicker.show(this.getParentFragmentManager(), "AccommodIQ");

            dateRangePicker.addOnNegativeButtonClickListener(v1 -> {
                dateRangePicker.dismiss();
            });

            dateRangePicker.addOnPositiveButtonClickListener(selection -> {
                dateFrom = selection.first;
                dateTo = selection.second;
                dateRangeSearch.setText(String.format("%s - %s", DateUtils.convertTimeToDate(selection.first), DateUtils.convertTimeToDate(selection.second)));
            });
        });

        searchBtn.setOnClickListener(v -> {
            String titleSearchText = !titleSearch.getText().toString().isEmpty() ? titleSearch.getText().toString() : null;
            String statusSearchText = (statusSearch.getSelectedItem().toString().equals("ALL")) ? null : statusSearch.getSelectedItem().toString();
            String roleString = type == ReservationListType.GUEST ? "guests" : "hosts";
            searchReservations(roleString, titleSearchText, dateFrom, dateTo, statusSearchText);
        });

        clearBtn.setOnClickListener(v -> {
            titleSearch.setText("");
            dateFrom = null;
            dateTo = null;
            dateRangeSearch.setText(R.string.date_range_hint);
            String roleString = type == ReservationListType.GUEST ? "guests" : "hosts";
            searchReservations(roleString, null, null, null, null);
        });
    }


}