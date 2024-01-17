package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.dtos.ReviewDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ReviewApiService {
    @GET("/hosts/{hostId}/reviews")
    Call<List<ReviewDto>> getHostReviews(@Path("hostId") Long hostId);
}
