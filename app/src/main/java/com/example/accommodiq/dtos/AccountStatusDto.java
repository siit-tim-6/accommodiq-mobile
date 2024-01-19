package com.example.accommodiq.dtos;

public class AccountStatusDto {
    private final String status;

    public AccountStatusDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
