package com.example.accommodiq.clients;

import com.example.accommodiq.dtos.UserReportDto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ReportsClient {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("reports")
    Call<ArrayList<UserReportDto>> getReports();
}
