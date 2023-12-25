package com.example.accommodiq.ui.account;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.accommodiq.BR;
import com.example.accommodiq.activities.LoginActivity;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.dtos.PasswordDto;
import com.example.accommodiq.models.Account;
import com.example.accommodiq.models.Password;
import com.example.accommodiq.services.interfaces.AccountApiService;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorizedProfileViewModel extends BaseObservable {
    private Account account = new Account();
    private final Password password = new Password();
    private boolean checked = false;
    private final AccountApiService accountApiService;

    public AuthorizedProfileViewModel(AccountApiService accountApiService) {
        this.accountApiService = accountApiService;

        Call<Account> call = accountApiService.getAccountDetails();
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(@NonNull Call<Account> call, @NonNull Response<Account> response) {
                account = response.body();
                assert account != null;
                notifyChange();
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

    @Bindable public String getOldPassword() {
        return password.getOldPassword();
    }

    public void setOldPassword(String value) {
        if (!Objects.equals(password.getOldPassword(), value))
            password.setOldPassword(value);

        notifyPropertyChanged(BR.oldPassword);
    }

    @Bindable public String getNewPassword() {
        return password.getNewPassword();
    }

    public void setNewPassword(String value) {
        if (!Objects.equals(password.getNewPassword(), value))
            password.setNewPassword(value);

        notifyPropertyChanged(BR.newPassword);
    }

    @Bindable public String getRepeatPassword() {
        return password.getRepeatPassword();
    }

    public void setRepeatPassword(String value) {
        if (!Objects.equals(password.getRepeatPassword(), value))
            password.setRepeatPassword(value);

        notifyPropertyChanged(BR.repeatPassword);
    }

    @Bindable public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean value) {
        if (checked != value)
            checked = value;

        notifyPropertyChanged(BR.checked);
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
        Call<Void> call = accountApiService.updateAccount(account);
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

        if (getChecked()) {
            Log.d("ACCOUNT", "change password");
            changePassword(view);
        }
    }

    public void changePassword(View view) {
        Call<Void> call = accountApiService.changePassword(new PasswordDto(getOldPassword(), getNewPassword()));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Log.d("Status Code", String.valueOf(response.code()));
                setOldPassword("");
                setNewPassword("");
                setRepeatPassword("");
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                setOldPassword("");
                setNewPassword("");
                setRepeatPassword("");
            }
        });
    }

    public void deleteAccount(View view) {
        Call<Void> call = accountApiService.deleteAccount();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                JwtUtils.clearJwtAndRole(view.getContext());
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                view.getContext().startActivity(intent);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {}
        });
    }
}
