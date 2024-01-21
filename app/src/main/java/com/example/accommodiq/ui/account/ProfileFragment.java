package com.example.accommodiq.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.accommodiq.R;
import com.example.accommodiq.activities.LoginActivity;
import com.example.accommodiq.adapters.ReviewsAdapter;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.dtos.AccountDetailsDto;
import com.example.accommodiq.dtos.ReviewDto;
import com.example.accommodiq.dtos.ReviewRequestDto;
import com.example.accommodiq.enums.AccountRole;
import com.google.firebase.messaging.FirebaseMessaging;

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
    private ListView reviewsList;
    private Long accountId;
    private Button buttonSignOut;
    private ArrayAdapter<ReviewDto> reviewsAdapter;
    private AccountDetailsDto accountDetails;
    private View addReviewLayout;
    private EditText editTextReview;
    private RatingBar ratingBarReview;
    private Button buttonSendReview;
    private View ratingLayout;
    private TextView reviewsTextView;

    public ProfileFragment() {
        // Required empty public constructor
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
        reviewsList = view.findViewById(R.id.reviews_list);
        buttonSignOut = view.findViewById(R.id.signOutBtn);
        buttonEditProfile.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_authorizedProfileFragment);
        });
        buttonReport.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong("accountId", accountId);
            Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_reportUserFragment, bundle);
        });
        buttonSignOut.setOnClickListener(v -> {
            Activity mainActivity = getActivity();
            if (mainActivity != null) {
                startActivity(new Intent(mainActivity, LoginActivity.class));
                mainActivity.finish();
                FirebaseMessaging.getInstance().unsubscribeFromTopic("user-" + JwtUtils.getLoggedInId(requireContext()));
                JwtUtils.clearJwtAndRole(requireContext());

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        addReviewLayout = view.findViewById(R.id.include_add_review);
        editTextReview = view.findViewById(R.id.editTextReview);
        ratingBarReview = view.findViewById(R.id.ratingBarReview);
        buttonSendReview = view.findViewById(R.id.buttonSendReview);
        ratingLayout = view.findViewById(R.id.rating_layout);
        reviewsTextView = view.findViewById(R.id.reviews_text_view);

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

        buttonSendReview.setOnClickListener( v ->  {
            String reviewComment = editTextReview.getText().toString();
            int starRating = (int) ratingBarReview.getRating();
            if(reviewComment.length()<3) {
                Toast.makeText(getContext(), "Review must be at least 3 characters long", Toast.LENGTH_SHORT).show();
                return;
            }
            if(starRating == 0) {
                Toast.makeText(getContext(), "Please select a rating", Toast.LENGTH_SHORT).show();
                return;
            }
            profileViewModel.sendReview(new ReviewRequestDto(starRating, reviewComment), accountId);
            editTextReview.setText("");
            ratingBarReview.setRating(0);
        });
    }

    private boolean canReportReview() {
        return JwtUtils.getLoggedInId(getContext()) == accountId && JwtUtils.getRole(getContext()) != null && JwtUtils.getRole(getContext()).equals("HOST");
    }

    private void updateUIButtonsVisibility() {
        if(JwtUtils.getLoggedInId(getContext()) != accountId) {
            buttonEditProfile.setVisibility(View.GONE);
            buttonSignOut.setVisibility(View.GONE);
            if(isAbleToReport()) {
                buttonReport.setVisibility(View.VISIBLE);
            } else {
                buttonReport.setVisibility(View.GONE);
            }
            if(JwtUtils.getRole(getContext()) != null && JwtUtils.getRole(getContext()).equals("GUEST") && accountDetails.getRole().equals(AccountRole.HOST)) {
                addReviewLayout.setVisibility(View.VISIBLE);
            } else {
                addReviewLayout.setVisibility(View.GONE);
            }
        }else {
            addReviewLayout.setVisibility(View.GONE);
            buttonReport.setVisibility(View.GONE);
            buttonEditProfile.setVisibility(View.VISIBLE);
            buttonSignOut.setVisibility(View.VISIBLE);
        }
    }

    private boolean isAbleToReport() {
        if(JwtUtils.getRole(getContext()) == null) {
            return false;
        }
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
        if(accountDetails.getRole().equals(AccountRole.GUEST)) {
            ratingLayout.setVisibility(View.GONE);
            addReviewLayout.setVisibility(View.GONE);
            reviewsTextView.setVisibility(View.GONE);
        }
    }

    private void updateReviewsList(List<ReviewDto> reviews) {
        reviewsAdapter.clear();
        reviewsAdapter.addAll(reviews);
        reviewsAdapter.notifyDataSetChanged();
        calculateRating(reviews);
    }

    private void calculateRating(List<ReviewDto> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            ratingBar.setRating(0);
            ratingText.setText("0");
            return;
        }

        float sum = 0;
        for (ReviewDto review : reviews) {
            sum += review.getRating();
        }

        float rating = sum / reviews.size();
        ratingBar.setRating(rating);
        ratingText.setText(String.format("%.2f", rating)+" "+reviews.size());
    }

    private void onReportReview(long reviewId) {
        profileViewModel.reportReview(reviewId);
    }

    private void onDeleteReview(long reviewId) {
        profileViewModel.deleteReview(reviewId);
    }

    private void onUserNameClicked(long accountId) {
        Bundle bundle = new Bundle();
        bundle.putLong("accountId", accountId);
        Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_self, bundle);
    }
}
