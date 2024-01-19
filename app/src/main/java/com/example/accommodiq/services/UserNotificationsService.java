package com.example.accommodiq.services;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.app.App;
import com.example.accommodiq.clients.NotificationsClient;
import com.example.accommodiq.dtos.NotificationDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class UserNotificationsService extends Service {

    private final long INTERVAL = 15 * 1000; // Fetch notifications every minute
    private NotificationsClient apiService;
    private Timer timer;
    private int notificationID = 50;
    private final String[] permissions = {Manifest.permission.POST_NOTIFICATIONS};


    private ArrayList<NotificationDto> notifications = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        this.apiService = RetrofitClientInstance.getRetrofitInstance(getApplicationContext()).create(NotificationsClient.class);
        fetchUserNotifications();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchUserNotifications();
            }
        }, 0, INTERVAL);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void showNotification(NotificationDto notification) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.getChannelID(notification.getType()))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(notification.getType().replace("_", " "))
                .setContentText(notification.getText())
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this.getApplicationContext(), permissions, 101);
            return;
        }
        notificationManager.notify(notificationID, builder.build());
    }


    private void fetchUserNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationDto n = new NotificationDto();
            n.setText("Bdjkajdkl jslkadjklsjad lajdklsj dlkadj sa");
            n.setType("HOST_REPLY_TO_REQUEST");
            showNotification(n);
        }
    }

    private void fetchUserNotifications2() {
        // Retrofit call to fetch notifications
        Call<List<NotificationDto>> call = apiService.getNotifications();
        call.enqueue(new Callback<List<NotificationDto>>() {
            @Override
            public void onResponse(Call<List<NotificationDto>> call, Response<List<NotificationDto>> response) {
                if (response.isSuccessful()) {
                    List<NotificationDto> notifications = response.body();
                    checkForNewNotificationDto(notifications);
                }
            }

            @Override
            public void onFailure(Call<List<NotificationDto>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void checkForNewNotificationDto(List<NotificationDto> notifications) {
        if (this.notifications.size() == notifications.size())
            return;

        this.notifications = (ArrayList<NotificationDto>) notifications;
        NotificationDto notificationToShow = this.notifications.get(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            showNotification(notificationToShow);
        }
    }
}
