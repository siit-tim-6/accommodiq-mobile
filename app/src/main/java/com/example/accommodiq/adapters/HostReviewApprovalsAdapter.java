package com.example.accommodiq.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.ReviewClient;
import com.example.accommodiq.databinding.HostReviewApprovalCardBinding;
import com.example.accommodiq.dtos.AccommodationStatusDto;
import com.example.accommodiq.dtos.HostReviewApprovalDto;
import com.example.accommodiq.ui.hostReviewApproval.HostReviewApprovalBaseObservable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostReviewApprovalsAdapter extends ArrayAdapter<HostReviewApprovalDto> {
    private final Context context;
    private final ArrayList<HostReviewApprovalDto> reviews;
    private final ReviewClient client;

    public HostReviewApprovalsAdapter(Context context, ArrayList<HostReviewApprovalDto> reviews) {
        super(context, R.layout.fragment_host_review_approval_list, reviews);
        this.client = RetrofitClientInstance.getRetrofitInstance(context).create(ReviewClient.class);
        this.context = context;
        this.reviews = reviews;
        this.fetchReviews();
    }

    private void fetchReviews() {
        Call<List<HostReviewApprovalDto>> call = client.getHostReviewsByStatus("REPORTED");
        call.enqueue(new Callback<List<HostReviewApprovalDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<HostReviewApprovalDto>> call, @NonNull Response<List<HostReviewApprovalDto>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                assert response.body() != null;
                reviews.addAll(response.body());
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<HostReviewApprovalDto>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HostReviewApprovalDto review = reviews.get(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.host_review_approval_card, parent, false);
        HostReviewApprovalCardBinding binding = HostReviewApprovalCardBinding.bind(convertView);
        binding.setObservable(new HostReviewApprovalBaseObservable(review));

        binding.confirmButton.setOnClickListener(v -> {
            Call<Void> call = client.changeReviewStatus(review.getReview().getId(), new AccommodationStatusDto("ACCEPTED"));
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "Error " + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    remove(review);
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
        });

        binding.dismissButton.setOnClickListener(v -> {
            Call<Void> call = client.deleteReview(review.getReview().getId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "Error " + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    remove(review);
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
        });
        return binding.getRoot();
    }

    @Override
    public HostReviewApprovalDto getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public void remove(@Nullable HostReviewApprovalDto object) {
        reviews.remove(object);
        notifyDataSetChanged();
    }

}
