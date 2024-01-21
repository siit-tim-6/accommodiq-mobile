package com.example.accommodiq.ui.notifications;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.accommodiq.dtos.NotificationSettingDto;

import java.util.Arrays;
import java.util.stream.Collectors;

public class NotificationSettingBaseObservable extends BaseObservable {
    private final NotificationSettingDto notificationSettingDto;

    public NotificationSettingBaseObservable(NotificationSettingDto notificationSettingDto) {
        this.notificationSettingDto = notificationSettingDto;
    }

    @Bindable
    public NotificationSettingDto getNotificationSettingDto() {
        return notificationSettingDto;
    }

    @Bindable
    public String getNotificationType() {
        return Arrays.stream(notificationSettingDto.getType().split("_"))
                .filter(word -> !word.isEmpty())
                .map(String::toLowerCase)
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
