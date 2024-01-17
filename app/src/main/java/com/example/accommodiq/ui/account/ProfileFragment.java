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
import android.widget.Toast;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.ReviewsAdapter;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.dtos.AccountDetailsDto;
import com.example.accommodiq.dtos.ReviewDto;
import com.example.accommodiq.enums.AccountRole;
import com.example.accommodiq.fragments.FragmentTransition;
import com.example.accommodiq.ui.report.ReportFragment;
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
    private AccountDetailsDto accountDetails;

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
        buttonEditProfile.setOnClickListener(v -> {
            FragmentTransition.to(new AuthorizedProfileFragment(), getActivity(), true, R.id.my_profile_fragment);
        });
        buttonReport.setOnClickListener(v -> {
            FragmentTransition.to(new ReportFragment(), getActivity(), true, R.id.my_profile_fragment);
        });
        buttonFinancialReport.setOnClickListener(v -> {
            //FragmentTransition.to(new FinancialReportFragment(), getActivity(), true, R.id.my_profile_fragment);
            Toast.makeText(getContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reviewsAdapter = new ReviewsAdapter(getContext(), new ArrayList<>(),
                this::onReportReview, this::onDeleteReview,this::onUserNameClicked, canReportReview());
        reviewsList.setAdapter(reviewsAdapter);

        profileViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ProfileViewModel(getContext());
            }
        }).get(ProfileViewModel.class);

        profileViewModel.getMessageLiveData().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                profileViewModel.clearMessage();
            }
        });

        if (accountId != null) {
            profileViewModel.loadAccountDetails(accountId);
        }

        profileViewModel.getAccountDetailsLiveData().observe(getViewLifecycleOwner(), accountDetails -> {
            this.accountDetails = accountDetails;
            updateUIWithAccountDetails(accountDetails);
            updateUIButtonsVisibility();
        });

        profileViewModel.getReviewsLiveData().observe(getViewLifecycleOwner(), reviews -> {
            updateReviewsList(reviews);
        });
    }

    private boolean canReportReview() {
        return JwtUtils.getLoggedInId(getContext()) == accountId && JwtUtils.getRole(getContext()).equals("HOST");
    }

    private void updateUIButtonsVisibility() {
        if(JwtUtils.getLoggedInId(getContext()) != accountId) {
            buttonEditProfile.setVisibility(View.GONE);
            buttonFinancialReport.setVisibility(View.GONE);
            if(isAbleToReport()) {
                buttonReport.setVisibility(View.VISIBLE);
            }
        }else {
            buttonReport.setVisibility(View.GONE);
            buttonEditProfile.setVisibility(View.VISIBLE);
            if(accountDetails.getRole().equals(AccountRole.HOST)) {
                buttonFinancialReport.setVisibility(View.VISIBLE);
            }else {
                buttonFinancialReport.setVisibility(View.GONE);
            }
        }
    }

    private boolean isAbleToReport() {
        if(accountDetails.getRole().equals(AccountRole.HOST) && JwtUtils.getRole(getContext()).equals("GUEST")) {
            return true;
        }
        if(accountDetails.getRole().equals(AccountRole.GUEST) && JwtUtils.getRole(getContext()).equals("HOST")) {
            return true;
        }
        return false;
    }

    private void updateUIWithAccountDetails(AccountDetailsDto accountDetails) {
        fullNameTextView.setText(accountDetails.getFirstName() + " " + accountDetails.getLastName());
        emailTextView.setText(accountDetails.getEmail());
        addressTextView.setText(accountDetails.getAddress());
        phoneNumberTextView.setText(accountDetails.getPhoneNumber());
        roleTextView.setText(accountDetails.getRole().toString());

        if (accountDetails.getRole().equals(AccountRole.HOST)) {
            profileViewModel.loadHostReviews(accountId);
        }
    }

    private void updateReviewsList(List<ReviewDto> reviews) {
        reviewsAdapter.clear();
        reviewsAdapter.addAll(reviews);
        reviewsAdapter.notifyDataSetChanged();
    }

    private void onReportReview(long reviewId) {
        profileViewModel.reportReview(reviewId);
    }

    private void onDeleteReview(long reviewId) {
        profileViewModel.deleteReview(reviewId);
    }

    private void onUserNameClicked(long userId) {
        FragmentTransition.to(ProfileFragment.newInstance(userId), getActivity(), true, R.id.my_profile_fragment);
    }
}
