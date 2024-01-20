package com.example.accommodiq.adapters;

import android.content.Context;
import android.os.Bundle;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.AccommodationClient;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.dtos.AccommodationStatusDto;
import com.example.accommodiq.dtos.GuestFavoriteDto;
import com.example.accommodiq.dtos.ModifyAccommodationDto;
import com.example.accommodiq.enums.AccommodationListType;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationListAdapter extends ArrayAdapter<AccommodationListDto> {
    private final ArrayList<AccommodationListDto> accommodations;
    private final Context context;
    private final AccommodationListType type;
    private final List<Long> favoriteIds = new ArrayList<>();
    private final List<ImageButton> favoriteButtons = new ArrayList<>();

    public AccommodationListAdapter(Context context, ArrayList<AccommodationListDto> accommodations, AccommodationListType type) {
        super(context, R.layout.accommodation_card, accommodations);
        this.accommodations = accommodations;
        this.context = context;
        this.type = type;
        fetchFavoriteIds();
    }

    private void fetchFavoriteIds() {
        if (!JwtUtils.getRole(context).equals("GUEST")) {
            return;
        }
        AccommodationClient accommodationClient = RetrofitClientInstance.getRetrofitInstance(context).create(AccommodationClient.class);
        Call<List<AccommodationListDto>> call = accommodationClient.getFavorites();
        call.enqueue(new Callback<List<AccommodationListDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<AccommodationListDto>> call, @NonNull Response<List<AccommodationListDto>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    favoriteIds.clear();
                    List<AccommodationListDto> accommodations = response.body();
                    accommodations.forEach(a -> favoriteIds.add(a.getId()));
                }
                favoriteButtons.forEach(b -> {
                    AccommodationListDto accommodation = (AccommodationListDto) b.getTag();
                    refreshFavoriteIcon(accommodation, b);
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<AccommodationListDto>> call, @NonNull Throwable t) {
            }
        });
    }

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
        AccommodationClient accommodationClient = RetrofitClientInstance.getRetrofitInstance(context).create(AccommodationClient.class);
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
                Picasso.get().load(imageUrl).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.accommodation_image);
            }
            titleTextView.setText(accommodation.getTitle());
            ratingBar.setRating(accommodation.getRating().floatValue());
            ratingTextView.setText(String.valueOf(accommodation.getRating()));
            reviewCountTextView.setText(reviewCount);
            locationTextView.setText(accommodation.getLocation().getAddress());
            guestsTextView.setText(guests);
            pricePerNightTextView.setText(pricePerNight);
            totalPriceTextView.setText(totalPrice);

            favoriteButton.setTag(accommodation);
            favoriteButtons.add(favoriteButton);

            favoriteButton.setOnClickListener(v -> {
                Call<Void> call = favoriteIds.contains(accommodation.getId()) ? accommodationClient.deleteFavorite(accommodation.getId()) : accommodationClient.addFavorite(new GuestFavoriteDto(accommodation.getId()));
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        fetchFavoriteIds();
                        refreshFavoriteIcon(accommodation, favoriteButton);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    }
                });
            });

            accommodationCard.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(v);
                Bundle args = new Bundle();
                args.putLong("accommodationId", accommodation.getId());
                int actionId = type == AccommodationListType.FAVORITES ?
                        R.id.action_navigation_favorites_to_navigation_accommodationDetails :
                        R.id.action_accommodationsListFragment_to_accommodationDetailsFragment;
                navController.navigate(actionId, args);
            });

            acceptButton.setOnClickListener(v -> {
                Call<AccommodationListDto> acceptAccommodationCall = accommodationClient.updateAccommodationStatus(accommodation.getId(), new AccommodationStatusDto("ACCEPTED"));
                changeStatus(accommodation, acceptAccommodationCall);
            });

            denyButton.setOnClickListener(v -> {
                Call<AccommodationListDto> denyAccommodationCall = accommodationClient.updateAccommodationStatus(accommodation.getId(), new AccommodationStatusDto("DENIED"));
                changeStatus(accommodation, denyAccommodationCall);
            });

            editButton.setOnClickListener(v -> accommodationClient.getAccommodationAdvancedDetails(accommodation.getId()).enqueue(new Callback<ModifyAccommodationDto>() {
                @Override
                public void onResponse(@NonNull Call<ModifyAccommodationDto> call, @NonNull Response<ModifyAccommodationDto> response) {
                    if (response.isSuccessful()) {
                        ModifyAccommodationDto accommodationDetailsDto = response.body();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("accommodationToModify", (Serializable) accommodationDetailsDto);
                        Navigation.findNavController(v).navigate(R.id.action_navigation_host_accommodations_to_navigation_new_accommodation, bundle);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ModifyAccommodationDto> call, @NonNull Throwable t) {
                }
            }));

            editAvailabilityButton.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putLong("accommodationId", accommodation.getId());
                Navigation.findNavController(v).navigate(R.id.action_navigation_host_accommodations_to_navigation_update_accommodation_availability, bundle);
            });

            switch (type) {
                case ADMIN_REVIEW_PENDING_ACCOMMODATIONS:
                    favoriteButton.setVisibility(View.GONE);

                    editButton.setVisibility(View.GONE);
                    editAvailabilityButton.setVisibility(View.GONE);

                    statusTextView.setVisibility(View.GONE);
                    totalPriceTextView.setVisibility(View.GONE);

                    acceptButton.setVisibility(View.VISIBLE);
                    denyButton.setVisibility(View.VISIBLE);
                    break;
                case SEARCH:
                    if (JwtUtils.getRole(context).equals("GUEST"))
                        favoriteButton.setVisibility(View.VISIBLE);
                    else
                        favoriteButton.setVisibility(View.GONE);

                    editButton.setVisibility(View.GONE);
                    editAvailabilityButton.setVisibility(View.GONE);

                    statusTextView.setVisibility(View.VISIBLE);
                    totalPriceTextView.setVisibility(View.GONE);

                    acceptButton.setVisibility(View.GONE);
                    denyButton.setVisibility(View.GONE);
                    break;
                case HOST_ACCOMMODATIONS:
                    favoriteButton.setVisibility(View.GONE);

                    editButton.setVisibility(View.VISIBLE);
                    editAvailabilityButton.setVisibility(View.VISIBLE);

                    statusTextView.setVisibility(View.VISIBLE);
                    totalPriceTextView.setVisibility(View.GONE);

                    acceptButton.setVisibility(View.GONE);
                    denyButton.setVisibility(View.GONE);
                    break;
                case FAVORITES:
                    favoriteButton.setVisibility(View.VISIBLE);

                    editButton.setVisibility(View.GONE);
                    editAvailabilityButton.setVisibility(View.GONE);

                    statusTextView.setVisibility(View.GONE);
                    totalPriceTextView.setVisibility(View.GONE);

                    acceptButton.setVisibility(View.GONE);
                    denyButton.setVisibility(View.GONE);
                    break;
            }
        }

        return convertView;
    }

    private void refreshFavoriteIcon(AccommodationListDto accommodation, ImageButton favoriteButton) {
        if (favoriteIds.contains(accommodation.getId())) {
            favoriteButton.setImageResource(R.drawable.icon_heart_fill);
        } else {
            favoriteButton.setImageResource(R.drawable.heart_icon);
        }
    }

    @Override
    public void remove(@Nullable AccommodationListDto object) {
        super.remove(object);
        notifyDataSetChanged();
    }

    private void changeStatus(AccommodationListDto accommodation, Call<AccommodationListDto> acceptAccommodationCall) {
        acceptAccommodationCall.enqueue(new Callback<AccommodationListDto>() {
            @Override
            public void onResponse(@NonNull Call<AccommodationListDto> call, @NonNull Response<AccommodationListDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, accommodation.getId() + ": changed", Toast.LENGTH_SHORT).show();
                    Optional<AccommodationListDto> result = accommodations.stream().filter(a -> a.getId() == accommodation.getId()).findFirst();
                    result.ifPresent(value -> remove(value));
                } else {
                    Toast.makeText(context, accommodation.getId() + ": change failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccommodationListDto> call, @NonNull Throwable t) {
            }
        });
    }
}
