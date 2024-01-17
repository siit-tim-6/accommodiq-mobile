package com.example.accommodiq.ui.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.ReviewsAdapter;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.dtos.ReviewDto;
import com.example.accommodiq.enums.AccountRole;
import com.example.accommodiq.ui.updateAccommodationAvailability.UpdateAccommodationAvailabilityViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private TextView fullNameTextView;
    private TextView emailTextView;
    private TextView addressTextView;
    private TextView phoneNumberTextView;
    private TextView roleTextView;
    private RatingBar ratingBar;
    private TextView ratingText;
    private Button buttonEditProfile;
    private Button buttonReport;
    private Button buttonFinancialReport;
    private ListView reviewsList;
    private Long accountId;
    private ArrayAdapter<ReviewDto> reviewsAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProfileFragment newInstance(@Nullable Long accountId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        if (accountId != null) {
            args.putLong("accountId", accountId);
        }
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("accountId")) {
            accountId = args.getLong("accountId");
        } else {
            accountId = JwtUtils.getLoggedInId(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fullNameTextView = view.findViewById(R.id.full_name_text_view);
        emailTextView = view.findViewById(R.id.email_text_view);
        addressTextView = view.findViewById(R.id.address_text_view);
        phoneNumberTextView = view.findViewById(R.id.phone_number_text_view);
        roleTextView = view.findViewById(R.id.role_text_view);
        ratingBar = view.findViewById(R.id.ratingBar);
        ratingText = view.findViewById(R.id.ratingText);
        buttonEditProfile = view.findViewById(R.id.buttonEditProfile);
        buttonReport = view.findViewById(R.id.buttonReport);
        buttonFinancialReport = view.findViewById(R.id.buttonFinancialReport);
        reviewsList = view.findViewById(R.id.reviews_list);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reviewsAdapter = new ReviewsAdapter(getContext(), new ArrayList<>());
        reviewsList.setAdapter(reviewsAdapter);

        profileViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ProfileViewModel(getContext());
            }
        }).get(ProfileViewModel.class);

        if (accountId != null) {
            profileViewModel.loadAccountDetails(accountId);
        }

        profileViewModel.getAccountDetailsLiveData().observe(getViewLifecycleOwner(), accountDetails -> {
            fullNameTextView.setText(accountDetails.getFirstName() + " " + accountDetails.getLastName());
            emailTextView.setText(accountDetails.getEmail());
            addressTextView.setText(accountDetails.getAddress());
            phoneNumberTextView.setText(accountDetails.getPhoneNumber());
            roleTextView.setText(accountDetails.getRole().toString());

            if (accountDetails.getRole().equals(AccountRole.HOST)) {
                profileViewModel.loadHostReviews(accountId);
            }
        });

        profileViewModel.getReviewsLiveData().observe(getViewLifecycleOwner(), reviews -> {
            updateReviewsList(reviews);
        });
    }

    private void updateReviewsList(List<ReviewDto> reviews) {
        reviewsAdapter.clear();
        reviewsAdapter.addAll(reviews);
        reviewsAdapter.notifyDataSetChanged();
    }


    // Additional methods for event handling and data binding
}
