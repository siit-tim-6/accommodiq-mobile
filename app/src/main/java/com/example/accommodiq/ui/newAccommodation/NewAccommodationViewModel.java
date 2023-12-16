package com.example.accommodiq.ui.newAccommodation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewAccommodationViewModel extends ViewModel {
       private final MutableLiveData<String> mText;

       public NewAccommodationViewModel() {
           mText = new MutableLiveData<>();
           mText.setValue("This is new accommodation fragment");
       }

       public LiveData<String> getText() { return mText; }
}