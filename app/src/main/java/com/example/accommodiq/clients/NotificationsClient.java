package com.example.accommodiq.clients;

import com.example.accommodiq.dtos.NotificationDto;
import com.example.accommodiq.dtos.NotificationSettingDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
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
    @PUT("users/notifications/{id}/seen")
    Call<Void> seenNotification(@Path("id") long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET("users/notification-settings")
    Call<List<NotificationSettingDto>> getNotificationSettings();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @PUT("users/notification-settings")
    Call<Void> updateNotificationSettings(@Body List<NotificationSettingDto> notificationSettings);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @PUT("users/notifications/seen")
    Call<Void> seenAllNotifications();
}
