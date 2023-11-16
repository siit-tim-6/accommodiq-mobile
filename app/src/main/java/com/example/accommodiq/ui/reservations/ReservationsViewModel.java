package com.example.accommodiq.ui.reservations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReservationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ReservationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is reservations fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}