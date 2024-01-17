package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReviewDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReviewApiService {
    @GET("/hosts/{hostId}/reviews")
    Call<List<ReviewDto>> getHostReviews(@Path("hostId") Long hostId);

    @PUT("/reviews/{reviewId}/report")
    Call<MessageDto> reportReview(@Path("reviewId") Long reviewId);

    @DELETE("/reviews/{reviewId}")
    Call<MessageDto> deleteReview(@Path("reviewId") Long reviewId);
}
