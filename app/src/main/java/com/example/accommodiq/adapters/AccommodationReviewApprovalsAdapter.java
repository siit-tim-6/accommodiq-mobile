package com.example.accommodiq.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.AccommodationClient;
import com.example.accommodiq.databinding.AccommodationReviewApprovalCardBinding;
import com.example.accommodiq.dtos.AccommodationReviewApprovalDto;
import com.example.accommodiq.ui.accommodationReviewApproval.AccommodationReviewApprovalBaseObservable;

import java.util.ArrayList;

public class AccommodationReviewApprovalsAdapter extends ArrayAdapter<AccommodationReviewApprovalDto> {
    private final Context context;
    private final ArrayList<AccommodationReviewApprovalDto> reviews;

    public AccommodationReviewApprovalsAdapter(Context context, ArrayList<AccommodationReviewApprovalDto> reviews) {
        super(context, R.layout.fragment_accommodation_review_approval_list, reviews);
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public AccommodationReviewApprovalDto getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reviews.get(position).hashCode();
    }

    @Override
    public void remove(@Nullable AccommodationReviewApprovalDto object) {
        reviews.remove(object);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AccommodationClient client = RetrofitClientInstance.getRetrofitInstance(context).create(AccommodationClient.class);
        AccommodationReviewApprovalDto review = reviews.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_accommodation_review_approval_list, parent, false);
        }
        AccommodationReviewApprovalCardBinding binding = AccommodationReviewApprovalCardBinding.bind(convertView);
        binding.setObservable(new AccommodationReviewApprovalBaseObservable(review));

        return binding.getRoot();
    }

}
