package com.example.accommodiq.ui.updateAccommodationAvailability;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.accommodiq.dtos.AccommodationBookingDetailFormDto;
import com.example.accommodiq.dtos.AccommodationBookingDetailsDto;
import com.example.accommodiq.dtos.AccommodationDetailsDto;
import com.example.accommodiq.dtos.AvailabilityDto;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.models.Availability;
import com.example.accommodiq.services.interfaces.AccommodationApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateAccommodationAvailabilityViewModel extends ViewModel {
    private MutableLiveData<List<Availability>> availabilityListLiveData;
    private AccommodationApiService apiService;

    public UpdateAccommodationAvailabilityViewModel() {
        availabilityListLiveData = new MutableLiveData<>();
        initApiService();
        // Optionally load initial data here if needed
    }

    private void initApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(AccommodationApiService.class);
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
}