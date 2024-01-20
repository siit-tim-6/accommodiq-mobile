package com.example.accommodiq.dtos;

public class ReportRequestDto {
    private String reason;

    public ReportRequestDto(String reason) {
        this.reason = reason;
    }

    public ReportRequestDto() {
        super();
    }

    public String getReason() {
        return reason;
    }
}
