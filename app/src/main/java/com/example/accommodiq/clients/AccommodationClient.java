package com.example.accommodiq.clients;

import com.example.accommodiq.dtos.AccommodationDetailsDto;
import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.dtos.AccommodationPriceDto;
import com.example.accommodiq.models.Accommodation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccommodationClient {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("accommodations")
    Call<List<AccommodationListDto>> getAccommodations(@Query("title") String title, @Query("location") String location, @Query("availableFrom") Long availableFrom, @Query("availableTo") Long availableTo,
                                                      @Query("priceFrom") Integer priceFrom, @Query("priceTo") Integer priceTo, @Query("guests") Integer guests, @Query("type") String type, @Query("benefits") String benefits);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("accommodations/{accommodationId}")
    Call<AccommodationDetailsDto> getAccommodationDetails(@Path("accommodationId") long accommodationId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("accommodations/{accommodationId}/total-price")
    Call<AccommodationPriceDto> getTotalPrice(@Path("accommodationId") long accommodationId, @Query("dateFrom") long dateFrom, @Query("dateTo") long dateTo, @Query("guests") int guests);
}
