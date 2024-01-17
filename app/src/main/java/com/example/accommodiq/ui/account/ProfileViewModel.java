package com.example.accommodiq.ui.account;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.dtos.AccountDetailsDto;
import com.example.accommodiq.services.interfaces.AccountApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {
    private final AccountApiService apiService;
    private MutableLiveData<AccountDetailsDto> accountDetailsLiveData;

    public ProfileViewModel(Context context) {
        apiService = RetrofitClientInstance.getRetrofitInstance(context).create(AccountApiService.class);
        accountDetailsLiveData = new MutableLiveData<>();
    }

    public LiveData<AccountDetailsDto> getAccountDetailsLiveData() {
        return accountDetailsLiveData;
    }

    public void loadAccountDetails(Long userId) {
        Call<AccountDetailsDto> call = apiService.getAccountDetailsById(userId);
        call.enqueue(new Callback<AccountDetailsDto>() {
            @Override
            public void onResponse(Call<AccountDetailsDto> call, Response<AccountDetailsDto> response) {
                if (response.isSuccessful()) {
                    accountDetailsLiveData.postValue(response.body());
                } else {
                    // Handle the error
                }
            }

            @Override
            public void onFailure(Call<AccountDetailsDto> call, Throwable t) {
                // Handle network errors
            }
        });
    }
}

