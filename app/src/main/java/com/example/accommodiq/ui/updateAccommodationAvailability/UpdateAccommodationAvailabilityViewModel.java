package com.example.accommodiq.ui.updateAccommodationAvailability;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.dtos.AccommodationBookingDetailFormDto;
import com.example.accommodiq.dtos.AccommodationBookingDetailsDto;
import com.example.accommodiq.dtos.AccommodationDetailsDto;
import com.example.accommodiq.dtos.AvailabilityDto;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.models.Availability;
import com.example.accommodiq.services.interfaces.AccommodationApiService;
import com.example.accommodiq.services.interfaces.AccountApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateAccommodationAvailabilityViewModel extends ViewModel {
    private MutableLiveData<List<Availability>> availabilityListLiveData;
    private AccommodationApiService apiService;

    public UpdateAccommodationAvailabilityViewModel(Context context) {
        availabilityListLiveData = new MutableLiveData<>();
        apiService = RetrofitClientInstance.getRetrofitInstance(context).create(AccommodationApiService.class);
        // Optionally load initial data here if needed
    }

    public LiveData<List<Availability>> getAvailabilityListLiveData() {
        return availabilityListLiveData;
    }

    public void setAvailabilityList(List<Availability> availabilityList) {
        availabilityListLiveData.setValue(availabilityList);
    }

    public void updateAccommodationBookingDetails(Long accommodationId,AccommodationBookingDetailsDto updateDto, Callback<AccommodationDetailsDto> callback) {
        Call<AccommodationDetailsDto> call = apiService.updateAccommodationBookingDetails(accommodationId, updateDto);
        call.enqueue(callback);
    }

    public void getAccommodationBookingDetails(Long accommodationId, Callback<AccommodationBookingDetailFormDto> callback) {
        Call<AccommodationBookingDetailFormDto> call = apiService.getAccommodationBookingDetails(accommodationId);
        call.enqueue(callback);
    }

    public void addAccommodationAvailability(Long accommodationId, AvailabilityDto availabilityDto, Callback<List<Availability>> callback) {
        Call<List<Availability>> call = apiService.addAccommodationAvailability(accommodationId,availabilityDto);
        call.enqueue(callback);
    }

    public void removeAccommodationAvailability(Long accommodationId, Long availabilityId, Callback<MessageDto> callback) {
        Call<MessageDto> call = apiService.removeAccommodationAvailability(accommodationId,availabilityId);
        call.enqueue(callback);
    }

    public void removeAvailabilityFromList(Long availabilityId) {
        List<Availability> currentList = availabilityListLiveData.getValue();
        if (currentList != null) {
            currentList.removeIf(availability -> availability.getId().equals(availabilityId));
            availabilityListLiveData.setValue(currentList);
        }
    }
}