package com.example.accommodiq.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.accommodiq.models.Account;

public class MyProfileViewModel extends ViewModel {
    private final MutableLiveData<String> emailLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> passwordLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> firstNameLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> lastNameLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> addressLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> phoneNumberLiveData = new MutableLiveData<>();

    public MyProfileViewModel() {
        Account account = new Account(
                "example@email.com",
                "secretpassword",
                "John",
                "Doe",
                "123 Main Street",
                "555-1234",
                Account.AccountType.GUEST
        );

        // Initialize LiveData with initial values
        emailLiveData.setValue(account.getEmail());
        passwordLiveData.setValue(account.getPassword());
        firstNameLiveData.setValue(account.getFirstName());
        lastNameLiveData.setValue(account.getLastName());
        addressLiveData.setValue(account.getAddress());
        phoneNumberLiveData.setValue(account.getPhoneNumber());
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
}
