package com.example.accommodiq.clients;

import com.example.accommodiq.dtos.AccommodationReviewApprovalDto;
import com.example.accommodiq.dtos.AccommodationStatusDto;
import com.example.accommodiq.dtos.HostReviewApprovalDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewClient {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("/accommodations/reviews")
    Call<List<AccommodationReviewApprovalDto>> getReviewsByStatus(@Query("status") String status);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @DELETE("/reviews/{id}")
    Call<Void> deleteReview(@Path("id") long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @PUT("/accommodations/reviews/{id}/status")
    Call<Void> changeReviewStatus(@Path("id") long id, @Body AccommodationStatusDto status);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("/hosts/reviews")
    Call<List<HostReviewApprovalDto>> getHostReviewsByStatus(@Query("status") String reported);
}
