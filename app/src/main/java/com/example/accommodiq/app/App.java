package com.example.accommodiq.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class App extends Application {
    public static final String HostReplyToRequestChannelID = "Host replies to request";
    public static final String AccommodationRatingChannelID = "Accommodation rating";
    public static final String HostRatingChannelID = "Host rating";
    public static final String ReservationRequestChannelID = "Reservation request";
    public static final String ReservationCancelChannelID = "Reservation cancel";
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();

    }

    private void createNotificationChannels() {
        NotificationChannel hostReplyToRequestChannel = new NotificationChannel(
                HostReplyToRequestChannelID,
                "Host replies to request",
                NotificationManager.IMPORTANCE_HIGH
        );
        hostReplyToRequestChannel.setDescription("Host replies to request");

        NotificationChannel accommodationRatingChannel = new NotificationChannel(
                AccommodationRatingChannelID,
                "Accommodation rating",
                NotificationManager.IMPORTANCE_HIGH
        );
        accommodationRatingChannel.setDescription("Accommodation rating");

        NotificationChannel hostRatingChannel = new NotificationChannel(
                HostRatingChannelID,
                "Host rating",
                NotificationManager.IMPORTANCE_HIGH
        );
        hostRatingChannel.setDescription("Host rating");

        NotificationChannel reservationRequestChannel = new NotificationChannel(
                ReservationRequestChannelID,
                "Reservation request",
                NotificationManager.IMPORTANCE_HIGH
        );
        reservationRequestChannel.setDescription("Reservation request");

        NotificationChannel reservationCancelChannel = new NotificationChannel(
                ReservationCancelChannelID,
                "Reservation cancel",
                NotificationManager.IMPORTANCE_HIGH
        );
        reservationCancelChannel.setDescription("Reservation cancel");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(hostReplyToRequestChannel);
        manager.createNotificationChannel(accommodationRatingChannel);
        manager.createNotificationChannel(hostRatingChannel);
        manager.createNotificationChannel(reservationRequestChannel);
        manager.createNotificationChannel(reservationCancelChannel);
    }

    public static String getChannelID(String type) {
        switch (type) {
            case "HOST_REPLY_TO_REQUEST":
                return HostReplyToRequestChannelID;
            case "ACCOMMODATION_RATING":
                return AccommodationRatingChannelID;
            case "HOST_RATING":
                return HostRatingChannelID;
            case "RESERVATION_REQUEST":
                return ReservationRequestChannelID;
            case "RESERVATION_CANCEL":
                return ReservationCancelChannelID;
            default:
                return "";
        }
    }
}
