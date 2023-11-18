package com.example.accommodiq.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.ReviewsListAdapter;
import com.example.accommodiq.models.Review;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.locks.ReentrantLock;

public class AccommodationDetailsFragment extends Fragment {
    private ArrayList<Review> reviews = new ArrayList<>();

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
        ListView reviewsListView = view.findViewById(R.id.reviews_list);

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

        prepareReviewList();
        reviewsListView.setAdapter(new ReviewsListAdapter(getActivity(), reviews));

        return view;
    }

    private String convertTimeToDate(Long time) {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.setTimeInMillis(time);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return format.format(utc.getTime());
    }

    private void prepareReviewList() {
        reviews.add(new Review(1, "John Doe", 5.0, "25/12/2023", "Great accommodation!"));
        reviews.add(new Review(2, "Balsa Bulatovic", 4.0, "24/12/2023", "Great accommodation test!"));
        reviews.add(new Review(3, "Marko Bulat", 4.5, "23/12/2023", "Great!"));
        reviews.add(new Review(4, "Teodor Vidakovic", 5.0, "22/12/2023", "Amazing!"));
    }
}
