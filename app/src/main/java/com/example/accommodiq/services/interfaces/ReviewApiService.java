package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReviewDto;
import com.example.accommodiq.dtos.ReviewRequestDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReviewApiService {
    @GET("/hosts/{hostId}/reviews")
    Call<List<ReviewDto>> getHostReviews(@Path("hostId") Long hostId);

    @PUT("/reviews/{reviewId}/report")
    Call<MessageDto> reportReview(@Path("reviewId") Long reviewId);

    @DELETE("/reviews/{reviewId}")
    Call<MessageDto> deleteReview(@Path("reviewId") Long reviewId);

    @POST("/hosts/{hostId}/reviews")
    Call<ReviewDto> addHostReview(@Path("hostId") Long hostId, @Body ReviewRequestDto review);

    @POST("/accommodations/{accommodationId}/reviews")
    Call<ReviewDto> addAccommodationReview(@Path("accommodationId") Long accommodationId, @Body ReviewRequestDto review);
}
