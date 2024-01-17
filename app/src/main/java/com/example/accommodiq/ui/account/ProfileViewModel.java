package com.example.accommodiq.ui.account;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.dtos.AccountDetailsDto;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReviewDto;
import com.example.accommodiq.services.interfaces.AccountApiService;
import com.example.accommodiq.services.interfaces.ReviewApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {
    private final AccountApiService apiService;
    private MutableLiveData<AccountDetailsDto> accountDetailsLiveData;
    private MutableLiveData<List<ReviewDto>> reviewsLiveData = new MutableLiveData<>();
    private MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private ReviewApiService reviewApiService;


    public ProfileViewModel(Context context) {
        apiService = RetrofitClientInstance.getRetrofitInstance(context).create(AccountApiService.class);
        reviewApiService = RetrofitClientInstance.getRetrofitInstance(context).create(ReviewApiService.class);
        accountDetailsLiveData = new MutableLiveData<>();
    }

    public LiveData<AccountDetailsDto> getAccountDetailsLiveData() {
        return accountDetailsLiveData;
    }

    public LiveData<List<ReviewDto>> getReviewsLiveData() {
        return reviewsLiveData;
    }

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public void loadAccountDetails(Long userId) {
        Call<AccountDetailsDto> call = apiService.getAccountDetailsById(userId);
        call.enqueue(new Callback<AccountDetailsDto>() {
            @Override
            public void onResponse(Call<AccountDetailsDto> call, Response<AccountDetailsDto> response) {
                if (response.isSuccessful()) {
                    accountDetailsLiveData.postValue(response.body());
                } else {
                    messageLiveData.postValue("Failed to load account details");
                }
            }

            @Override
            public void onFailure(Call<AccountDetailsDto> call, Throwable t) {
                messageLiveData.postValue("Network error: " + t.getMessage());
            }
        });
    }

    public void loadHostReviews(Long hostId) {
        Call<List<ReviewDto>> call = reviewApiService.getHostReviews(hostId);
        call.enqueue(new Callback<List<ReviewDto>>() {
            @Override
            public void onResponse(Call<List<ReviewDto>> call, Response<List<ReviewDto>> response) {
                if (response.isSuccessful()) {
                    reviewsLiveData.postValue(response.body());
                } else {
                    messageLiveData.postValue("Failed to load host reviews");
                }
            }

            @Override
            public void onFailure(Call<List<ReviewDto>> call, Throwable t) {
                messageLiveData.postValue("Network error: " + t.getMessage());
            }
        });
    }

    public void reportReview(long reviewId) {
        reviewApiService.reportReview(reviewId).enqueue(new Callback<MessageDto>() {
            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                messageLiveData.postValue(response.body().getMessage());
            }

            @Override
            public void onFailure(Call<MessageDto> call, Throwable t) {
                messageLiveData.postValue("Network error: " + t.getMessage());
            }
        });
    }

    public void deleteReview(long reviewId) {
        reviewApiService.deleteReview(reviewId).enqueue(new Callback<MessageDto>() {
            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                messageLiveData.postValue(response.body().getMessage());
            }

            @Override
            public void onFailure(Call<MessageDto> call, Throwable t) {
                messageLiveData.postValue("Network error: " + t.getMessage());
            }
        });
    }

    public void clearMessage() {
        messageLiveData.setValue(null); // Clear the message
    }

}

