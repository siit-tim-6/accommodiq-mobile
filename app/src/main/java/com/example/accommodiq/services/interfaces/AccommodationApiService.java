package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.dtos.AccommodationBookingDetailFormDto;
import com.example.accommodiq.dtos.AccommodationBookingDetailsDto;
import com.example.accommodiq.dtos.AccommodationDetailsDto;
import com.example.accommodiq.dtos.AccommodationReviewDto;
import com.example.accommodiq.dtos.AvailabilityDto;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ModifyAccommodationDto;
import com.example.accommodiq.models.Availability;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AccommodationApiService {
    @Multipart
    @POST("/images")
    Call<List<String>> uploadImages(@Part List<MultipartBody.Part> images);

    @POST("hosts/accommodations")
    Call<AccommodationDetailsDto> createNewAccommodation(@Body ModifyAccommodationDto accommodation);

    @PUT("/accommodations/{accommodationId}/booking-details")
    Call<AccommodationDetailsDto> updateAccommodationBookingDetails(@Path("accommodationId") Long accommodationId, @Body AccommodationBookingDetailsDto updateDto);

    @GET("/accommodations/{accommodationId}/booking-details")
    Call<AccommodationBookingDetailFormDto> getAccommodationBookingDetails(@Path("accommodationId") Long accommodationId);

    @POST("/accommodations/{accommodationId}/availabilities")
    Call<List<Availability>> addAccommodationAvailability(@Path("accommodationId") Long accommodationId, @Body AvailabilityDto availabilityDto);

    @DELETE("/accommodations/{accommodationId}/availabilities/{availabilityId}")
    Call<MessageDto> removeAccommodationAvailability(@Path("accommodationId") Long accommodationId, @Path("availabilityId") Long availabilityId);

    @PUT("/accommodations/{accommodationId}")
    Call<AccommodationDetailsDto> updateAccommodation(ModifyAccommodationDto dto);
}
