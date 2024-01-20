package com.example.accommodiq.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.AccommodationListAdapter;
import com.example.accommodiq.adapters.ReviewsAdapter;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.AccommodationClient;
import com.example.accommodiq.clients.GuestClient;
import com.example.accommodiq.dtos.AccommodationDetailsDto;
import com.example.accommodiq.dtos.AccommodationDetailsReviewDto;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.dtos.AccommodationPriceDto;
import com.example.accommodiq.dtos.ErrorResponseDto;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReservationRequestDto;
import com.example.accommodiq.dtos.ReviewDto;
import com.example.accommodiq.dtos.ReviewRequestDto;
import com.example.accommodiq.listener.OnDeleteClickListener;
import com.example.accommodiq.listener.OnReportClickListener;
import com.example.accommodiq.listener.OnUserNameClickListener;
import com.example.accommodiq.models.Review;
import com.example.accommodiq.services.interfaces.ReviewApiService;
import com.example.accommodiq.ui.account.ProfileFragment;
import com.example.accommodiq.utils.DateUtils;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationDetailsFragment extends Fragment {
    private AccommodationDetailsDto accommodation;
    private long accommodationId;
    private AccommodationClient accommodationClient;
    private GuestClient guestClient;
    private Long dateFrom = null;
    private Long dateTo = null;
    private ReviewApiService reviewApiService;

    public AccommodationDetailsFragment() { }

    public AccommodationDetailsFragment(long accommodationId) {
        this.accommodationId = accommodationId;
        this.accommodationClient = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(AccommodationClient.class);
        this.guestClient = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(GuestClient.class);
        this.reviewApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(ReviewApiService.class);
    }

    public static AccommodationDetailsFragment newInstance(long accommodationId) {
        return new AccommodationDetailsFragment(accommodationId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("accommodationId") && accommodationClient == null ) {
            accommodationId = getArguments().getLong("accommodationId");
            accommodationClient = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(AccommodationClient.class);
            guestClient = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(GuestClient.class);
            reviewApiService = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(ReviewApiService.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceBundle) {
        return inflater.inflate(R.layout.fragment_accommodation_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton favoriteImageButton = view.findViewById(R.id.favorite_button);
        TextView dateRangeTextView = view.findViewById(R.id.date_range_text);
        Button dateRangePickerButton = view.findViewById(R.id.date_range_picker_button);
        ListView reviewsListView = view.findViewById(R.id.reviews_list);
        TextView titleTextView = view.findViewById(R.id.accommodation_details_title);
        TextView hostNameTextView = view.findViewById(R.id.accommodation_details_host);
        RatingBar hostRatingStars = view.findViewById(R.id.accommodation_details_host_stars);
        TextView hostRatingTextView = view.findViewById(R.id.accommodation_details_host_rating);
        TextView hostReviewCountTextView = view.findViewById(R.id.accommodation_details_host_review_count);
        RatingBar ratingStars = view.findViewById(R.id.accommodation_details_rating_stars);
        TextView ratingTextView = view.findViewById(R.id.accommodation_details_rating);
        TextView reviewCountTextView = view.findViewById(R.id.accommodation_details_review_count);
        TextView guestsTextView = view.findViewById(R.id.accommodation_details_guests);
        TextView benefitsTextView = view.findViewById(R.id.accommodation_details_benefits);
        TextView descriptionTextView = view.findViewById(R.id.accommodation_details_description);
        TextView locationTextView = view.findViewById(R.id.accommodation_details_location);
        TextView minPriceTextView = view.findViewById(R.id.accommodation_details_min_price);
        ImageView imageView = view.findViewById(R.id.accommodation_details_image);
        TextView totalPriceTextView = view.findViewById(R.id.accommodation_details_total_price);
        EditText guestsField = view.findViewById(R.id.accommodation_details_guests_field);
        Button reserveButton = view.findViewById(R.id.accommodation_details_reserve);
        View addReviewLayout = view.findViewById(R.id.include_add_review);
        EditText editTextReview = view.findViewById(R.id.editTextReview);
        RatingBar ratingBarReview = view.findViewById(R.id.ratingBarReview);
        Button buttonSendReview = view.findViewById(R.id.buttonSendReview);

        if (JwtUtils.getRole(getActivity()) == null || !JwtUtils.getRole(getActivity()).equals("GUEST")) {
            addReviewLayout.setVisibility(View.GONE);
        }

        hostNameTextView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong("accountId", accommodation.getHost().getId());
            Navigation.findNavController(getView()).navigate(R.id.action_accommodationDetailsFragment_to_profileFragment, bundle);
        });

        favoriteImageButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Added to favorites!", Toast.LENGTH_SHORT).show();
        });

        dateRangePickerButton.setOnClickListener(v -> {
            CalendarConstraints dateValidator = (new CalendarConstraints.Builder()).setValidator(DateValidatorPointForward.now()).build();
            MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker().setTheme(R.style.ThemeMaterialCalendar)
                    .setCalendarConstraints(dateValidator)
                    .setTitleText("Select reservation check-in and check-out date").setSelection(new Pair<>(null, null)).build();

            if (!dateRangePicker.isAdded())
                dateRangePicker.show(this.getParentFragmentManager(), "AccommodIQ");

            dateRangePicker.addOnNegativeButtonClickListener(v1 -> {
                dateRangePicker.dismiss();
            });

            dateRangePicker.addOnPositiveButtonClickListener(selection -> {
                dateRangeTextView.setText(String.format("%s - %s", DateUtils.convertTimeToDate(selection.first), DateUtils.convertTimeToDate(selection.second)));
                dateFrom = selection.first;
                dateTo = selection.second;

                if (accommodation != null && dateFrom != null && dateTo != null) {
                    int guestsInput;

                    if (accommodation.getPricingType().equals("PER_NIGHT")) {
                        guestsInput = 0;
                    } else {
                        try {
                            guestsInput = Integer.parseInt(guestsField.getText().toString());

                            if (guestsInput < accommodation.getMinGuests() || guestsInput > accommodation.getMaxGuests()) {
                                totalPriceTextView.setText(R.string.total_price);
                                return;
                            }
                        } catch (Exception ignored) {
                            totalPriceTextView.setText(R.string.total_price);
                            return;
                        }
                    }

                    Call<AccommodationPriceDto> accommodationPriceCall = accommodationClient.getTotalPrice(accommodationId, dateFrom, dateTo, guestsInput);
                    accommodationPriceCall.enqueue(new Callback<AccommodationPriceDto>() {
                        @Override
                        public void onResponse(Call<AccommodationPriceDto> call, Response<AccommodationPriceDto> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String totalPrice = ((AccommodationPriceDto) response.body()).getTotalPrice() + "€ total";
                                totalPriceTextView.setText(totalPrice);
                            } else {
                                totalPriceTextView.setText(R.string.total_price);
                            }
                        }

                        @Override
                        public void onFailure(Call<AccommodationPriceDto> call, Throwable t) {
                            totalPriceTextView.setText(R.string.total_price);
                        }
                    });
                }
            });
        });

        buttonSendReview.setOnClickListener( v ->  {
            String reviewComment = editTextReview.getText().toString();
            int starRating = (int) ratingBarReview.getRating();
            if(reviewComment.length()<3){
                Toast.makeText(getContext(), "Review must be at least 3 characters long", Toast.LENGTH_SHORT).show();
                return;
            }
            if(starRating==0){
                Toast.makeText(getContext(), "Please select a rating", Toast.LENGTH_SHORT).show();
                return;
            }
            sendReview(new ReviewRequestDto(starRating, reviewComment), accommodation.getId());
            editTextReview.setText("");
            ratingBarReview.setRating(0);
        });

        Call<AccommodationDetailsDto> accommodationDetailsCall = accommodationClient.getAccommodationDetails(accommodationId);
        accommodationDetailsCall.enqueue(new Callback<AccommodationDetailsDto>() {
            @Override
            public void onResponse(Call<AccommodationDetailsDto> call, Response<AccommodationDetailsDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    accommodation = (AccommodationDetailsDto) response.body();
                    if (accommodation.getImages().size() > 0) {
                        String imageUrl = RetrofitClientInstance.getServerIp(getContext()) + "/images/" + accommodation.getImages().get(0);
                        Picasso.get().load(imageUrl).into(imageView);
                    } else {
                        imageView.setImageResource(R.drawable.accommodation_image);
                    }
                    titleTextView.setText(accommodation.getTitle());
                    String hostName = "Hosted by " + accommodation.getHost().getName();
                    hostNameTextView.setText(hostName);
                    hostRatingStars.setRating(((float) accommodation.getHost().getRating()));
                    hostRatingTextView.setText(String.valueOf(accommodation.getHost().getRating()));
                    String hostReviewCount = "(" + accommodation.getHost().getReviewCount() + ")";
                    hostReviewCountTextView.setText(hostReviewCount);
                    ratingStars.setRating(((float) accommodation.getRating()));
                    ratingTextView.setText(String.valueOf(accommodation.getRating()));
                    String reviewCount = "(" + accommodation.getReviewCount() + ")";
                    reviewCountTextView.setText(reviewCount);
                    String guests = accommodation.getMinGuests() + "-" + accommodation.getMaxGuests() + " guests";
                    guestsTextView.setText(guests);
                    benefitsTextView.setText(String.join(", ", accommodation.getBenefits()));
                    descriptionTextView.setText(accommodation.getDescription());
                    locationTextView.setText(accommodation.getLocation().getAddress());
                    String minPrice = "From " + accommodation.getMinPrice() + ((accommodation.getPricingType().equals("PER_GUEST")) ? " / guest" : "") + " / night";
                    minPriceTextView.setText(minPrice);
                    boolean canReport = accommodation.getHost().getId() == JwtUtils.getLoggedInId(getActivity());
                    ReviewsAdapter reviewsAdapter = new ReviewsAdapter(
                            getContext(),
                            accommodation.getReviews(),
                            reportClickListener,
                            deleteClickListener,
                            userNameClickListener,
                            canReport
                    );
                    reviewsListView.setAdapter(reviewsAdapter);
                } else {
                    Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccommodationDetailsDto> call, Throwable t) {
                Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
            }
        });

        guestsField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                int guestsInput;
                try {
                    guestsInput = Integer.parseInt(guestsField.getText().toString());
                } catch (Exception ignored) {
                    totalPriceTextView.setText(R.string.total_price);
                    return;
                }

                if (accommodation != null && accommodation.getPricingType().equals("PER_GUEST")
                        && dateFrom != null && dateTo != null) {

                    if (guestsInput < accommodation.getMinGuests() || guestsInput > accommodation.getMaxGuests()) {
                        totalPriceTextView.setText(R.string.total_price);
                        return;
                    }

                    Call<AccommodationPriceDto> accommodationPriceCall = accommodationClient.getTotalPrice(accommodationId, dateFrom, dateTo, guestsInput);

                    accommodationPriceCall.enqueue(new Callback<AccommodationPriceDto>() {
                        @Override
                        public void onResponse(Call<AccommodationPriceDto> call, Response<AccommodationPriceDto> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String totalPrice = ((AccommodationPriceDto) response.body()).getTotalPrice() + "€ total";
                                totalPriceTextView.setText(totalPrice);
                            } else {
                                totalPriceTextView.setText(R.string.total_price);
                            }
                        }

                        @Override
                        public void onFailure(Call<AccommodationPriceDto> call, Throwable t) {
                            totalPriceTextView.setText(R.string.total_price);
                        }
                    });
                }
            }
        });

        reserveButton.setOnClickListener(v -> {
            if (dateFrom == null || dateTo == null || guestsField.getText().toString().isEmpty() || guestsField.getText() == null) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int guestsValue;
            try {
                guestsValue = Integer.parseInt(guestsField.getText().toString());
            } catch (Exception ignored) {
                Toast.makeText(getContext(), "Guests filed is not a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (guestsValue < accommodation.getMinGuests() || guestsValue > accommodation.getMaxGuests()) {
                Toast.makeText(getContext(), "Invalid number of guests", Toast.LENGTH_SHORT).show();
                return;
            }

            long loggedInId = JwtUtils.getLoggedInId(getActivity());
            if (JwtUtils.getRole(getActivity()) == null || !JwtUtils.getRole(getActivity()).equals("GUEST") || loggedInId == -1) {
                Toast.makeText(getContext(), "You must be a logged in user", Toast.LENGTH_SHORT).show();
                return;
            }

            ReservationRequestDto requestDto = new ReservationRequestDto(dateFrom, dateTo, guestsValue, accommodationId);
            Call<ReservationRequestDto> createReservationCall = guestClient.createReservation(requestDto);
            createReservationCall.enqueue(new Callback<ReservationRequestDto>() {
                @Override
                public void onResponse(Call<ReservationRequestDto> call, Response<ReservationRequestDto> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Reservation successfully created!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Accommodation is not available within selected date range", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ReservationRequestDto> call, Throwable t) {
                    Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void sendReview(ReviewRequestDto reviewRequestDto, long accommodationId) {
        reviewApiService.addAccommodationReview(accommodationId, reviewRequestDto).enqueue(new Callback<ReviewDto>() {
            @Override
            public void onResponse(Call<ReviewDto> call, Response<ReviewDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Review added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = "";
                    if (response.errorBody() != null) {
                        try {
                            ErrorResponseDto errorResponse = new Gson().fromJson(response.errorBody().charStream(), ErrorResponseDto.class);
                            errorMessage += ": " + errorResponse.getMessage();
                        } catch (Exception e) {
                            errorMessage += "Error parsing error message";
                        }
                    }
                    Log.d("Review", errorMessage);
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewDto> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private OnReportClickListener reportClickListener = reviewId -> {
        reviewApiService.reportReview(reviewId).enqueue(new Callback<MessageDto>() {
            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MessageDto> call, Throwable t) {
                Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
            }
        });
    };

    private OnDeleteClickListener deleteClickListener = reviewId -> {
        reviewApiService.deleteReview(reviewId).enqueue(new Callback<MessageDto>() {
            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                accommodation.getReviews().removeIf(review -> review.getId() == reviewId);
                ((ReviewsAdapter) ((ListView) getView().findViewById(R.id.reviews_list)).getAdapter()).notifyDataSetChanged();
                calculateRating();
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            private void calculateRating() {
                float rating = 0;
                for ( ReviewDto review : accommodation.getReviews()) {
                    rating += review.getRating();
                }
                rating /= accommodation.getReviews().size();
                accommodation.setRating(rating);
                ((TextView) getView().findViewById(R.id.accommodation_details_rating)).setText(String.valueOf(rating));
                ((RatingBar) getView().findViewById(R.id.accommodation_details_rating_stars)).setRating(rating);
            }

            @Override
            public void onFailure(Call<MessageDto> call, Throwable t) {
                Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
            }
        });
    };

    private OnUserNameClickListener userNameClickListener = accountId -> {
        Bundle bundle = new Bundle();
        bundle.putLong("accountId", accountId);
        Navigation.findNavController(getView()).navigate(R.id.action_accommodationDetailsFragment_to_profileFragment, bundle);
    };
}
