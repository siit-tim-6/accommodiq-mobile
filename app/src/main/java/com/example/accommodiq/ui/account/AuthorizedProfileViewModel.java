package com.example.accommodiq.ui.account;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.accommodiq.clients.ClientUtils;
import com.example.accommodiq.models.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.databinding.BaseObservable;

public class AuthorizedProfileViewModel extends ViewModel {
    private final MutableLiveData<String> emailLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> passwordLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> firstNameLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> lastNameLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> addressLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> phoneNumberLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showChangePasswordLiveData = new MutableLiveData<>();
    private final String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob3N0M0BleGFtcGxlLmNvbSIsImV4cCI6MTcwMjE5NTA2NiwiaWF0IjoxNzAyMTc3MDY2fQ.uyQn2nJmvuSwTNMJ3_BYUFFTgwS_fAyXaj82w45Ld7-wuKKFFDghUyEt0MJqY76eHby95jYYBl_wIran8dVWsQ";

    public AuthorizedProfileViewModel() {
        Call<Account> call = ClientUtils.accountService.getAccountDetails("Bearer " + token);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(@NonNull Call<Account> call, @NonNull Response<Account> response) {
                Account account = response.body();
                assert account != null;
                emailLiveData.setValue(account.getEmail());
                firstNameLiveData.setValue(account.getFirstName());
                lastNameLiveData.setValue(account.getLastName());
                addressLiveData.setValue(account.getAddress());
                phoneNumberLiveData.setValue(account.getPhoneNumber());
            }

            @Override
            public void onFailure(@NonNull Call<Account> call, @NonNull Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    // Getter methods for LiveData
    public LiveData<String> getEmailLiveData() {
        return emailLiveData;
    }

    public LiveData<String> getPasswordLiveData() {
        return passwordLiveData;
    }

    public LiveData<String> getFirstNameLiveData() {
        return firstNameLiveData;
    }

    public LiveData<String> getLastNameLiveData() {
        return lastNameLiveData;
    }

    public LiveData<String> getAddressLiveData() {
        return addressLiveData;
    }

    public LiveData<String> getPhoneNumberLiveData() {
        return phoneNumberLiveData;
    }

    public LiveData<Boolean> getShowChangePasswordLiveData() {
        return showChangePasswordLiveData;
    }

    public void updateAccountDetails(View view) {
        String email = emailLiveData.getValue();
        String firstName = firstNameLiveData.getValue();
        String lastName = lastNameLiveData.getValue();
        String address = addressLiveData.getValue();
        String phoneNumber = phoneNumberLiveData.getValue();

        assert email != null;
        Account account = new Account(email, null, firstName, lastName, address, phoneNumber, null);
        Log.d("ACCOUNT", account.toString());
        Call<Void> call = ClientUtils.accountService.updateAccount("Bearer " + token, account);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Log.d("Status Code", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}
