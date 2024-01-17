package com.example.accommodiq.adapters;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.accommodiq.R;
import com.example.accommodiq.dtos.ReviewDto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewsAdapter extends ArrayAdapter<ReviewDto> {

    public ReviewsAdapter(Context context, List<ReviewDto> reviews) {
        super(context, 0, reviews);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_card, parent, false);
        }

        ReviewDto review = getItem(position);

        TextView userName = convertView.findViewById(R.id.review_user_name);
        RatingBar ratingBar = convertView.findViewById(R.id.review_rating);
        TextView reviewDate = convertView.findViewById(R.id.review_date);
        TextView reviewContent = convertView.findViewById(R.id.review_content);
        ImageView reportIcon = convertView.findViewById(R.id.imageViewReport);
        ImageView deleteIcon = convertView.findViewById(R.id.imageViewDelete);

        userName.setText(review.getAuthor());
        ratingBar.setRating(review.getRating());
        // Format and set the review date
        reviewDate.setText(formatDate(review.getDate()));
        reviewContent.setText(review.getComment());

        // Set visibility and click listeners for report and delete icons
        reportIcon.setVisibility(review.isDeletable() ? View.VISIBLE : View.GONE);
        deleteIcon.setVisibility(review.isDeletable() ? View.VISIBLE : View.GONE);

        reportIcon.setOnClickListener(v -> {
            // Handle report action
        });

        deleteIcon.setOnClickListener(v -> {
            // Handle delete action
        });

        return convertView;
    }

    private String formatDate(Long timestamp) {
        // Format the date as needed
        if (timestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        }
        return "";
    }
}

