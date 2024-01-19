package com.example.accommodiq.clients;

import com.example.accommodiq.dtos.AccountStatusDto;
import com.example.accommodiq.dtos.UserReportDto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReportsClient {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("reports")
    Call<ArrayList<UserReportDto>> getReports();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @PUT("users/{id}/status")
    Call<Void> blockUser(@Path("id") long id, @Body AccountStatusDto status);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @DELETE("reports/{id}")
    Call<Void> deleteReport(@Path("id") long id);
}
