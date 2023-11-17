package com.example.accommodiq.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.accommodiq.R;
import com.example.accommodiq.fragments.AccommodationDetailsFragment;
import com.example.accommodiq.fragments.FragmentTransition;
import com.example.accommodiq.models.Accommodation;

import java.util.ArrayList;

public class AccommodationListAdapter extends ArrayAdapter<Accommodation> {
    private ArrayList<Accommodation> accommodations;
    private Context context;

    public AccommodationListAdapter(Context context, ArrayList<Accommodation> accommodations) {
        super(context, R.layout.accommodation_card, accommodations);
        this.accommodations = accommodations;
        this.context = context;
    }

    @Override
    public int getCount() {
        return accommodations.size();
    }

    @Nullable
    @Override
    public Accommodation getItem(int position) {
        return accommodations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Accommodation accommodation = getItem(position);
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

        if (accommodation != null) {
            String reviewCount = "(" + accommodation.getReviewCount() + ")";
            String guests = accommodation.getMinGuests() + "-" + accommodation.getMaxGuests() + " guests";
            String pricePerNight = accommodation.getPrice() + "€ / night";
            String totalPrice = accommodation.getPrice() + "€ total";

            imageView.setImageResource(accommodation.getImage());
            titleTextView.setText(accommodation.getTitle());
            ratingBar.setRating(((float) accommodation.getRating()));
            ratingTextView.setText(String.valueOf(accommodation.getRating()));
            reviewCountTextView.setText(reviewCount);
            locationTextView.setText(accommodation.getLocation());
            guestsTextView.setText(guests);
            pricePerNightTextView.setText(pricePerNight);
            totalPriceTextView.setText(totalPrice);
            accommodationCard.setOnClickListener(v -> {
                FragmentTransition.to(AccommodationDetailsFragment.newInstance(), (FragmentActivity) context, true, R.id.fragment_main);
                Log.i("AccommodIQ", "Clicked accommodation card with id: " + accommodation.getId());
            });
        }

        return convertView;
    }
}
