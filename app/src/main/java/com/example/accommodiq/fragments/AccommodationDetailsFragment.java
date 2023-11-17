package com.example.accommodiq.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.example.accommodiq.R;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class AccommodationDetailsFragment extends Fragment {

    public AccommodationDetailsFragment() {

    }

    public static AccommodationDetailsFragment newInstance() {
        AccommodationDetailsFragment fragment = new AccommodationDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceBundle) {
        View view = inflater.inflate(R.layout.fragment_accommodation_details, container, false);
        ImageButton favoriteImageButton = view.findViewById(R.id.favorite_button);
        TextView dateRangeTextView = view.findViewById(R.id.date_range_text);
        Button dateRangePickerButton = view.findViewById(R.id.date_range_picker_button);

        favoriteImageButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Added to favorites!", Toast.LENGTH_SHORT).show();
        });

        dateRangePickerButton.setOnClickListener(v -> {
            MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker().setTheme(R.style.ThemeMaterialCalendar)
                    .setTitleText("Select reservation check-in and check-out date").setSelection(new Pair<>(null, null)).build();

            dateRangePicker.show(this.getParentFragmentManager(), "AccommodIQ");

            dateRangePicker.addOnNegativeButtonClickListener(v1 -> {
                dateRangePicker.dismiss();
            });

            dateRangePicker.addOnPositiveButtonClickListener(selection -> {
                dateRangeTextView.setText(String.format("%s - %s", convertTimeToDate(selection.first), convertTimeToDate(selection.second)));
            });
        });
        return view;
    }

    private String convertTimeToDate(Long time) {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.setTimeInMillis(time);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return format.format(utc.getTime());
    }
}
