package com.example.accommodiq.ui.newAccommodation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.accommodiq.models.Availability;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewAccommodationViewModel extends ViewModel {
    private final MutableLiveData<List<Availability>> availabilityListLiveData = new MutableLiveData<>();
    private List<Availability> availabilityList = new ArrayList<>();

    public NewAccommodationViewModel() {
        initSampleData();
    }

    private void initSampleData() {
        // Sample date initialization
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date startDate1 = dateFormat.parse("03/12/2023");
            Date endDate1 = dateFormat.parse("05/12/2023");
            availabilityList.add(new Availability(1L, startDate1.getTime(), endDate1.getTime(), 100.00));

            Date startDate2 = dateFormat.parse("10/12/2023");
            Date endDate2 = dateFormat.parse("15/12/2023");
            availabilityList.add(new Availability(2L, startDate2.getTime(), endDate2.getTime(), 150.00));

            Date startDate3 = dateFormat.parse("12/12/2023");
            Date endDate3 = dateFormat.parse("20/12/2023");
            availabilityList.add(new Availability(3L, startDate3.getTime(), endDate3.getTime(), 250.00));
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the error according to your needs
        }
        availabilityListLiveData.setValue(availabilityList);
    }

    public LiveData<List<Availability>> getAvailabilityListLiveData() {
        return availabilityListLiveData;
    }

    public void addAvailability(Availability availability) {
        availabilityList.add(availability);
        availabilityListLiveData.setValue(availabilityList);
    }
}