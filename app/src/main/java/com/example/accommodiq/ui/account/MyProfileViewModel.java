package com.example.accommodiq.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyProfileViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public MyProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
