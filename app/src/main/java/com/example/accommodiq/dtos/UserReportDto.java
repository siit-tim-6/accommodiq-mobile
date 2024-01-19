package com.example.accommodiq.dtos;

public class UserReportDto {
    Report_UserInfoDto reportedUser;
    Report_UserInfoDto reportingUser;
    String reason;
    long id;

    public Report_UserInfoDto getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(Report_UserInfoDto reportedUser) {
        this.reportedUser = reportedUser;
    }

    public Report_UserInfoDto getReportingUser() {
        return reportingUser;
    }

    public void setReportingUser(Report_UserInfoDto reportingUser) {
        this.reportingUser = reportingUser;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserReportDto() {
    }
}
