package com.example.accommodiq.dtos;

public class NotificationSettingDto {
    private String type;
    private boolean on;

    public NotificationSettingDto() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}
