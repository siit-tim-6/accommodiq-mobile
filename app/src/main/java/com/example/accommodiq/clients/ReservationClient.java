package com.example.accommodiq.clients;

import com.example.accommodiq.dtos.AccommodationListDto;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReservationCardDto;
import com.example.accommodiq.dtos.ReservationStatusDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservationClient {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("{roleString}/reservations")
    Call<List<ReservationCardDto>> getReservations(@Path("roleString") String roleString, @Query("title") String title, @Query("startDate") Long startDate,
                                                   @Query("endDate") Long endDate, @Query("status") String status);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("guests/reservations/cancellable")
    Call<List<Long>> getCancellableReservationIds();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @DELETE("reservations/{reservationId}")
    Call<MessageDto> delete(@Path("reservationId") Long reservationId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @PUT("reservations/{reservationId}/status")
    Call<ReservationCardDto> changeStatus(@Path("reservationId") Long reservationId, @Body ReservationStatusDto status);
}
