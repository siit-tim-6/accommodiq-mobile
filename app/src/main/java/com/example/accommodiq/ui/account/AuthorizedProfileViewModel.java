package com.example.accommodiq.ui.account;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import com.example.accommodiq.clients.ClientUtils;
import com.example.accommodiq.models.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.databinding.BaseObservable;

import com.example.accommodiq.BR;

import java.util.Objects;

public class AuthorizedProfileViewModel extends BaseObservable {
    private Account account;
    private Boolean showChangePassword = false;
    private final String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob3N0M0BleGFtcGxlLmNvbSIsImV4cCI6MTcwMjE5NTA2NiwiaWF0IjoxNzAyMTc3MDY2fQ.uyQn2nJmvuSwTNMJ3_BYUFFTgwS_fAyXaj82w45Ld7-wuKKFFDghUyEt0MJqY76eHby95jYYBl_wIran8dVWsQ";

    public AuthorizedProfileViewModel() {
        Call<Account> call = ClientUtils.accountService.getAccountDetails("Bearer " + token);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(@NonNull Call<Account> call, @NonNull Response<Account> response) {
                Account account = response.body();
                assert account != null;
                setEmail(account.getEmail());
                setAddress(account.getFirstName());
                setLastName(account.getLastName());
                setAddress(account.getAddress());
                setPhoneNumber(account.getPhoneNumber());
            }

            @Override
            public void onFailure(@NonNull Call<Account> call, @NonNull Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    // Getter methods for 
    @Bindable
    public String getEmail() {
        return account.getEmail();
    }

    public void setEmail(String value) {
        if (!Objects.equals(account.getEmail(), value))
            account.setEmail(value);

        notifyPropertyChanged(BR.email);
    }

    @Bindable public String getPassword() {
        return account.getPassword();
    }

    public void setPassword(String value) {
        if (!Objects.equals(account.getPassword(), value))
            account.setPassword(value);

        notifyPropertyChanged(BR.password);
    }

    @Bindable public String getFirstName() {
        return account.getFirstName();
    }

    public void setFirstName(String value) {
        if (!Objects.equals(account.getFirstName(), value))
            account.setFirstName(value);

        notifyPropertyChanged(BR.firstName);
    }

    @Bindable public String getLastName() {
        return account.getLastName();
    }

    public void setLastName(String value) {
        if (!Objects.equals(account.getLastName(), value))
            account.setLastName(value);

        notifyPropertyChanged(BR.lastName);
    }

    @Bindable public String getAddress() {
        return account.getAddress();
    }

    public void setAddress(String value) {
        if (!Objects.equals(account.getAddress(), value))
            account.setAddress(value);

        notifyPropertyChanged(BR.address);
    }

    @Bindable public String getPhoneNumber() {
        return account.getPhoneNumber();
    }

    public void setPhoneNumber(String value) {
        if (!Objects.equals(account.getPhoneNumber(), value))
            account.setPhoneNumber(value);

        notifyPropertyChanged(BR.phoneNumber);
    }

    public Boolean getShowChangePassword() {
        return showChangePassword;
    }

    public void setShowChangePassword(Boolean value) {
        if (!Objects.equals(showChangePassword, value))
            showChangePassword = value;
    }

    public void updateAccountDetails(View view) {
        String email = getEmail();
        String firstName = getFirstName();
        String lastName = getLastName();
        String address = getAddress();
        String phoneNumber = getPhoneNumber();

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
