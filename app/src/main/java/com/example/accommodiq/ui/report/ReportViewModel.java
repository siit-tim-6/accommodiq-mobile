package com.example.accommodiq.ui.report;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.dtos.ErrorResponseDto;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReportRequestDto;
import com.example.accommodiq.services.interfaces.AccountApiService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportViewModel extends ViewModel {
    private MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private AccountApiService accountApiService;

    public ReportViewModel(Context context) {
        accountApiService = RetrofitClientInstance.getRetrofitInstance(context).create(AccountApiService.class);
    }

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public void reportUser(Long accountId, ReportRequestDto reportRequest) {
        accountApiService.reportAccount(accountId, reportRequest).enqueue(new Callback<MessageDto>() {
            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                if (response.isSuccessful()) {
                    messageLiveData.postValue(response.body().getMessage());
                } else {
                    String errorMessage = "";
                    if (response.errorBody() != null) {
                        try {
                            ErrorResponseDto errorResponse = new Gson().fromJson(response.errorBody().charStream(), ErrorResponseDto.class);
                            errorMessage += errorResponse.getMessage();
                        } catch (Exception e) {
                            errorMessage += "Error parsing error message";
                        }
                    }
                    Log.d("Report:", errorMessage);
                    messageLiveData.postValue(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<MessageDto> call, Throwable t) {
                messageLiveData.postValue("Network error: " + t.getMessage());
            }
        });
    }
}