package com.example.accommodiq.clients;

import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.dtos.AccommodationTitleDto;
import com.example.accommodiq.dtos.FinancialReportEntryDto;
import com.example.accommodiq.dtos.FinancialReportMonthlyEntryDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FinancialReportClient {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("hosts/financial-report")
    Call<List<FinancialReportEntryDto>> getFinancialReport(@Query("fromDate") Long fromDate, @Query("toDate") Long toDate);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("accommodations/{accommodationId}/financial-report")
    Call<List<FinancialReportMonthlyEntryDto>> getAccommodationFinancialReport(@Path("accommodationId") Long accommodationId, @Query("year") int year);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("hosts/accommodation-titles")
    Call<List<AccommodationTitleDto>> getAccommodationTitles();
}
