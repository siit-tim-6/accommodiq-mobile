package com.example.accommodiq.adapters;

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
import com.example.accommodiq.clients.AccommodationClient;
import com.example.accommodiq.databinding.AccommodationReviewApprovalCardBinding;
import com.example.accommodiq.dtos.AccommodationReviewApprovalDto;
import com.example.accommodiq.dtos.AccommodationStatusDto;
import com.example.accommodiq.ui.accommodationReviewApproval.AccommodationReviewApprovalBaseObservable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationReviewApprovalsAdapter extends ArrayAdapter<AccommodationReviewApprovalDto> {
    private final Context context;
    private final ArrayList<AccommodationReviewApprovalDto> reviews;
    private final AccommodationClient client;

    public AccommodationReviewApprovalsAdapter(Context context, ArrayList<AccommodationReviewApprovalDto> reviews) {
        super(context, R.layout.fragment_accommodation_review_approval_list, reviews);
        this.client = RetrofitClientInstance.getRetrofitInstance(context).create(AccommodationClient.class);
        this.context = context;
        this.reviews = reviews;
        fetchReviews("PENDING");
        fetchReviews("REPORTED");
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
        AccommodationReviewApprovalDto reviewApproval = reviews.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_accommodation_review_approval_list, parent, false);
        }
        AccommodationReviewApprovalCardBinding binding = AccommodationReviewApprovalCardBinding.bind(convertView);
        binding.setObservable(new AccommodationReviewApprovalBaseObservable(reviewApproval));

        binding.approveButton.setOnClickListener(v -> {
            Call<Void> call = client.changeReviewStatus(reviewApproval.getReview().getId(), new AccommodationStatusDto("APPROVED"));
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    remove(reviewApproval);
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
        });

        binding.dismissButton.setOnClickListener(v -> {
            Call<Void> call = reviewApproval.getReview().getStatus().equals("PENDING")?
                    client.changeReviewStatus(reviewApproval.getReview().getId(), new AccommodationStatusDto("DECLINED")):
                    client.deleteReview(reviewApproval.getReview().getId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    remove(reviewApproval);
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

    private void fetchReviews(String status) {
        Call<List<AccommodationReviewApprovalDto>> call = client.getReviewsByStatus(new AccommodationStatusDto(status));
        call.enqueue(new Callback<List<AccommodationReviewApprovalDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<AccommodationReviewApprovalDto>> call, @NonNull Response<List<AccommodationReviewApprovalDto>> response) {
                assert response.body() != null;
                reviews.addAll(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<AccommodationReviewApprovalDto>> call, @NonNull Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}
