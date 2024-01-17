package com.example.accommodiq.adapters;

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
import com.example.accommodiq.listener.OnDeleteClickListener;
import com.example.accommodiq.listener.OnReportClickListener;
import com.example.accommodiq.listener.OnUserNameClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewsAdapter extends ArrayAdapter<ReviewDto> {
    private OnReportClickListener reportClickListener;
    private OnDeleteClickListener deleteClickListener;
    private OnUserNameClickListener userNameClickListener;
    private boolean canReport;

    public ReviewsAdapter(Context context, List<ReviewDto> reviews,
                          OnReportClickListener reportClickListener,
                          OnDeleteClickListener deleteClickListener,
                          OnUserNameClickListener userNameClickListener,boolean canReport) {
        super(context, 0, reviews);
        this.reportClickListener = reportClickListener;
        this.deleteClickListener = deleteClickListener;
        this.canReport = canReport;
        this.userNameClickListener = userNameClickListener;
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
        reviewDate.setText(formatDate(review.getDate()));
        reviewContent.setText(review.getComment());

        reportIcon.setVisibility(this.canReport ? View.VISIBLE : View.GONE);
        deleteIcon.setVisibility(review.isDeletable() ? View.VISIBLE : View.GONE);

        if (review != null) {
            long reviewId = review.getId();

            reportIcon.setOnClickListener(v -> {
                if (reportClickListener != null) {
                    reportClickListener.onReportClicked(reviewId);
                }
            });

            deleteIcon.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClicked(reviewId);
                }
            });

            userName.setOnClickListener(v -> {
                if (userNameClickListener != null) {
                    userNameClickListener.onUserNameClicked(review.getAuthorId());
                }
            });
        }

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

