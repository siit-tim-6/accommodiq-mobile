package com.example.accommodiq.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.dtos.AccommodationDetailsDto;
import com.example.accommodiq.dtos.AccommodationReviewDto;
import com.example.accommodiq.dtos.AccommodationStatusDto;
import com.example.accommodiq.dtos.ModifyAccommodationDto;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.fragments.AccommodationDetailsFragment;
import com.example.accommodiq.fragments.FragmentTransition;
import com.example.accommodiq.models.Accommodation;
import com.example.accommodiq.services.interfaces.AccommodationApiService;
import com.example.accommodiq.services.interfaces.AccommodationApiService;
import com.example.accommodiq.ui.newAccommodation.NewAccommodationFragment;
import com.example.accommodiq.ui.updateAccommodationAvailability.UpdateAccommodationAvailabilityFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationListAdapter extends ArrayAdapter<AccommodationListDto> {
    private ArrayList<AccommodationListDto> accommodations;
    private Context context;
//    private final boolean showAcceptDenyButtons;
//    private final boolean showStatus;

    public AccommodationListAdapter(Context context, ArrayList<AccommodationListDto> accommodations) {
        super(context, R.layout.accommodation_card, accommodations);
        this.accommodations = accommodations;
        this.context = context;
    }

//    public AccommodationListAdapter(Context context, ArrayList<Accommodation> accommodations, boolean showAcceptDenyButtons, boolean showStatus) {
//        super(context, R.layout.accommodation_card, accommodations);
//        this.accommodations = accommodations;
//        this.context = context;
//        this.showAcceptDenyButtons = showAcceptDenyButtons;
//        this.showStatus = showStatus;
//    }

    @Override
    public int getCount() {
        return accommodations.size();
    }

    @Nullable
    @Override
    public AccommodationListDto getItem(int position) {
        return accommodations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return accommodations.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        AccommodationApiService apiService = RetrofitClientInstance.getRetrofitInstance(context).create(AccommodationApiService.class);
        AccommodationListDto accommodation = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_card, parent, false);
        }
        LinearLayout accommodationCard = convertView.findViewById(R.id.accommodation_card_item);
        ImageView imageView = convertView.findViewById(R.id.accommodation_image);
        TextView titleTextView = convertView.findViewById(R.id.accommodation_title);
        RatingBar ratingBar = convertView.findViewById(R.id.accommodation_rating_stars);
        TextView ratingTextView = convertView.findViewById(R.id.accommodation_rating);
        TextView reviewCountTextView = convertView.findViewById(R.id.accommodation_review_count);
        TextView locationTextView = convertView.findViewById(R.id.accommodation_location);
        TextView guestsTextView = convertView.findViewById(R.id.accommodation_guests);
        TextView pricePerNightTextView = convertView.findViewById(R.id.accommodation_price_per_night);
        TextView totalPriceTextView = convertView.findViewById(R.id.accommodation_total_price);
        ImageButton favoriteButton = convertView.findViewById(R.id.favorite_button_card);
        TextView statusTextView = convertView.findViewById(R.id.accommodation_status);

        ImageButton acceptButton = convertView.findViewById(R.id.accommodation_accept_button);
        ImageButton denyButton = convertView.findViewById(R.id.accommodation_deny_button);
        ImageButton editButton = convertView.findViewById(R.id.accommodation_edit_button);
        ImageButton editAvailabilityButton = convertView.findViewById(R.id.accommodation_availability_edit_button);

        if (accommodation != null) {
            String reviewCount = "(" + accommodation.getReviewCount() + ")";
            String guests = accommodation.getMinGuests() + "-" + accommodation.getMaxGuests() + " guests";
            String pricePerNight = "From " + accommodation.getMinPrice() + "€" + (accommodation.getPricingType().equals("PER_GUEST") ? " / guest" : "") + " / night";
            String totalPrice = accommodation.getTotalPrice() + "€ total";

            if (!accommodation.getImage().isEmpty()) {
                String imageUrl = RetrofitClientInstance.getServerIp(getContext()) + "/images/" + accommodation.getImage();
                Picasso.with(getContext()).load(imageUrl).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.accommodation_image);
            }
            titleTextView.setText(accommodation.getTitle());
            ratingBar.setRating(accommodation.getRating().floatValue());
            ratingTextView.setText(String.valueOf(accommodation.getRating()));
            reviewCountTextView.setText(reviewCount);
            locationTextView.setText(accommodation.getLocation());
            guestsTextView.setText(guests);
            pricePerNightTextView.setText(pricePerNight);
            totalPriceTextView.setText(totalPrice);
            favoriteButton.setOnClickListener(v -> {
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
            });
            accommodationCard.setOnClickListener(v -> {
                FragmentTransition.to(AccommodationDetailsFragment.newInstance(accommodation.getId()), (FragmentActivity) context, true, R.id.accommodations_fragment);
            });

//            acceptButton.setOnClickListener(v -> {
//                Call<AccommodationReviewDto> acceptAccommodationCall = apiService.updateAccommodationStatus(accommodation.getId(), new AccommodationStatusDto("ACCEPTED"));
//                changeStatus(accommodation, acceptAccommodationCall);
//            });
//
//            denyButton.setOnClickListener(v -> {
//                Call<AccommodationReviewDto> denyAccommodationCall = apiService.updateAccommodationStatus(accommodation.getId(), new AccommodationStatusDto("DENIED"));
//                changeStatus(accommodation, denyAccommodationCall);
//            });
//
//            editButton.setOnClickListener(v -> {
//                apiService.getAccommodationDetails(accommodation.getId()).enqueue(new Callback<ModifyAccommodationDto>() {
//                    @Override
//                    public void onResponse(@NonNull Call<ModifyAccommodationDto> call, @NonNull Response<ModifyAccommodationDto> response) {
//                        if (response.isSuccessful()) {
//                            ModifyAccommodationDto accommodationDetailsDto = response.body();
//
//                            FragmentTransition.to(NewAccommodationFragment.newInstance(accommodationDetailsDto), (FragmentActivity) context, true, R.id.accommodations_fragment);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<ModifyAccommodationDto> call, @NonNull Throwable t) {
//                    }
//                });
//            });
//
//            editAvailabilityButton.setOnClickListener(v -> {
//                FragmentTransition.to(UpdateAccommodationAvailabilityFragment.newInstance(accommodation.getId()), (FragmentActivity) context, true, R.id.accommodations_fragment);
//            });
//
//            if (showAcceptDenyButtons) {
//                favoriteButton.setVisibility(View.GONE);
//                editButton.setVisibility(View.GONE);
//                editAvailabilityButton.setVisibility(View.GONE);
//            } else {
//                acceptButton.setVisibility(View.GONE);
//                denyButton.setVisibility(View.GONE);
//            }
//            if (showStatus) {
//                statusTextView.setVisibility(View.VISIBLE);
//                statusTextView.setText("STATUS: " + accommodation.getStatus());
//                editButton.setVisibility(View.VISIBLE);
//                editAvailabilityButton.setVisibility(View.VISIBLE);
//
//                favoriteButton.setVisibility(View.GONE);
//                acceptButton.setVisibility(View.GONE);
//                denyButton.setVisibility(View.GONE);
//            } else {
//                statusTextView.setVisibility(View.GONE);
//                editButton.setVisibility(View.GONE);
//                editAvailabilityButton.setVisibility(View.GONE);
//            }
        }

        return convertView;
    }

//    @Override
//    public void remove(@Nullable Accommodation object) {
//        super.remove(object);
//        notifyDataSetChanged();
//    }
//
//    private void changeStatus(Accommodation accommodation, Call<AccommodationReviewDto> acceptAccommodationCall) {
//        acceptAccommodationCall.enqueue(new Callback<AccommodationReviewDto>() {
//            @Override
//            public void onResponse(@NonNull Call<AccommodationReviewDto> call, @NonNull Response<AccommodationReviewDto> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(context, accommodation.getId() + ": changed", Toast.LENGTH_SHORT).show();
//                    Optional<Accommodation> result = accommodations.stream().filter(a -> a.getId() == accommodation.getId()).findFirst();
//                    result.ifPresent(value -> remove(value));
//                } else {
//                    Toast.makeText(context, accommodation.getId() + ": change failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<AccommodationReviewDto> call, @NonNull Throwable t) {
//            }
//        });
//    }
}
