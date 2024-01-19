package com.example.accommodiq.services;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.accommodiq.R;
import com.example.accommodiq.app.App;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class UserFirebaseService extends FirebaseMessagingService {
    private final String[] permissions = {Manifest.permission.POST_NOTIFICATIONS};

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle the message
        if (remoteMessage.getData().size() > 0) {
            // Handle data payload
            handleDataPayload(remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d("UserFirebaseService", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            // Handle notification payload
            handleNotificationPayload(remoteMessage.getNotification());

            // Trigger your custom action here
            // For example, create a notification
            createNotification(remoteMessage.getNotification());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        // Handle token refresh
        // For example, send the token to your server
    }

    private void handleDataPayload(Map<String, String> data) {
        // Handle data payload here
    }

    private void handleNotificationPayload(RemoteMessage.Notification notification) {
        // Handle notification payload here
    }

    private void createNotification(RemoteMessage.Notification notification) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.getChannelID(Objects.requireNonNull(notification.getTitle())))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(notification.getTitle().replace("_", " "))
                .setContentText(notification.getBody())
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this.getApplicationContext(), permissions, 101);
            return;
        }
        notificationManager.notify(new Random().nextInt(), builder.build());
    }
}

