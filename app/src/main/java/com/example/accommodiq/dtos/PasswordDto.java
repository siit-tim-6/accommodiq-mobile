package com.example.accommodiq.dtos;

import androidx.annotation.NonNull;

import com.example.accommodiq.models.Password;

public class PasswordDto {
    private String oldPassword;
    private String newPassword;

    public PasswordDto() {
    }

    public PasswordDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public PasswordDto(Password password) {
        this.oldPassword = password.getOldPassword();
        this.newPassword = password.getNewPassword();
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @NonNull
    @Override
    public String toString() {
        return "PasswordDto{" +
                "oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
