package com.example.accommodiq.ui.account;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.dtos.AccountDetailsDto;
import com.example.accommodiq.dtos.ErrorResponseDto;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReviewDto;
import com.example.accommodiq.dtos.ReviewRequestDto;
import com.example.accommodiq.services.interfaces.AccountApiService;
import com.example.accommodiq.services.interfaces.ReviewApiService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {
    private final AccountApiService apiService;
    private MutableLiveData<AccountDetailsDto> accountDetailsLiveData;
    private MutableLiveData<List<ReviewDto>> reviewsLiveData = new MutableLiveData<>(new ArrayList<>());
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

    public void sendReview(ReviewRequestDto reviewRequestDto, Long accountId) {
        reviewApiService.addHostReview(accountId, reviewRequestDto).enqueue(new Callback<ReviewDto>() {
            @Override
            public void onResponse(Call<ReviewDto> call, Response<ReviewDto> response) {
                if(response.isSuccessful()) {
                    messageLiveData.postValue("Review added successfully");
                    reviewsLiveData.postValue(new ArrayList<ReviewDto>(reviewsLiveData.getValue()) {{
                        add(response.body());
                    }});
                } else {
                    String errorMessage = "";
                    if (response.errorBody() != null) {
                        try {
                            ErrorResponseDto errorResponse = new Gson().fromJson(response.errorBody().charStream(), ErrorResponseDto.class);
                            errorMessage += ": " + errorResponse.getMessage();
                        } catch (Exception e) {
                            errorMessage += ": Error parsing error message";
                        }
                    }
                    Log.d("Review", errorMessage);
                    messageLiveData.postValue(errorMessage);
                }

            }

            @Override
            public void onFailure(Call<ReviewDto> call, Throwable t) {
                messageLiveData.postValue("Network error: " + t.getMessage());
            }
        });
    }
}

