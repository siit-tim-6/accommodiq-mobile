package com.example.accommodiq.clients;

import com.example.accommodiq.dtos.ReservationRequestDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GuestClient {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @POST("guests/{guestId}/reservations")
    Call<ReservationRequestDto> createReservation(@Path("guestId") long guestId, @Body ReservationRequestDto body);
}
