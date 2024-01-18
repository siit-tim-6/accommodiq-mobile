package com.example.accommodiq.clients;

import com.example.accommodiq.dtos.NotificationDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface NotificationsClient {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("users/notifications")
    Call<List<NotificationDto>> getNotifications();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("users/notifications/{id}/seen")
    Call<Void> seenNotification(@Path("id") long id);
}
