package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.dtos.AccommodationCreateDto;
import com.example.accommodiq.dtos.AccommodationDetailsDto;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccommodationApiService {
    @POST("/images")
    Call<List<String>> uploadImages(@Body List<MultipartBody.Part> images);

    @POST("hosts/accommodations")
    Call<AccommodationDetailsDto> createNewAccommodation(@Body AccommodationCreateDto accommodation);
}
