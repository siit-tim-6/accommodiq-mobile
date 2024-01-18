package com.example.accommodiq.ui.notifications;

import android.graphics.Color;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.accommodiq.dtos.NotificationDto;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

public class NotificationBaseObservable extends BaseObservable {
    private NotificationDto notificationDto;

    public NotificationBaseObservable(NotificationDto notificationDto) {
        this.notificationDto = notificationDto;
    }

    @Bindable
    public String getText() {
        return notificationDto.getText();
    }

    @Bindable
    public String getTime() {
        Date date = new Date(notificationDto.getTime());
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
        return dateFormat.format(date);
    }

    @Bindable
    public int getColor() {
        switch (notificationDto.getType()) {
            case "RESERVATION_REQUEST":
                return Color.parseColor("#0000FF");
            case "RESERVATION_CANCEL":
                return Color.parseColor("#FF0000");
            case "HOST_RATING":
                return Color.parseColor("#00FF00");
            case "ACCOMMODATION_RATING":
                return Color.parseColor("#FFA500");
            case "HOST_REPLY_TO_REQUEST":
                return Color.parseColor("#800080");
            default:
                return Color.parseColor("#000000");
        }
    }


    @Bindable
    public NotificationDto getNotificationDto() {
        return notificationDto;
    }
}
