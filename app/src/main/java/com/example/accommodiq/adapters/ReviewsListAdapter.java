package com.example.accommodiq.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.accommodiq.R;
import com.example.accommodiq.models.Review;

import java.util.ArrayList;

public class ReviewsListAdapter extends ArrayAdapter<Review> {
    private ArrayList<Review> reviews;

    public ReviewsListAdapter(Context context, ArrayList<Review> reviews) {
        super(context, R.layout.accommodation_card, reviews);
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Nullable
    @Override
    public Review getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Review review = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_card, parent, false);
        }

        TextView userNameTextView = convertView.findViewById(R.id.review_user_name);
        RatingBar rating = convertView.findViewById(R.id.review_rating);
        TextView dateTextView = convertView.findViewById(R.id.review_date);
        TextView contentTextView = convertView.findViewById(R.id.review_content);

        if (review != null) {
            userNameTextView.setText(review.getUserName());
            rating.setRating((float) review.getRating());
            dateTextView.setText(review.getDate());
            contentTextView.setText(review.getContent());
        }

        return convertView;
    }
}
