package com.example.accommodiq.ui.userReport;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.accommodiq.BR;
import com.example.accommodiq.dtos.Report_UserInfoDto;
import com.example.accommodiq.dtos.UserReportDto;

import java.util.Objects;

public class UserReportBaseObservable extends BaseObservable {
    UserReportDto userReportDto;

    @Bindable
    public String getReason() {
        return userReportDto.getReason();
    }

    public void setReason(String value) {
        if (!Objects.equals(userReportDto.getReason(), value))
            userReportDto.setReason(value);

        notifyPropertyChanged(BR.reason);
    }

    @Bindable
    public long getId() {
        return userReportDto.getId();
    }

    public void setId(long value) {
        if (userReportDto.getId() != value)
            userReportDto.setId(value);

        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public Report_UserInfoDto getReportedUser() {
        return userReportDto.getReportedUser();
    }

    public void setReportedUser(Report_UserInfoDto value) {
        if (!Objects.equals(userReportDto.getReportedUser(), value))
            userReportDto.setReportedUser(value);

        notifyPropertyChanged(BR.reportedUser);
    }

    @Bindable
    public Report_UserInfoDto getReportingUser() {
        return userReportDto.getReportingUser();
    }

    public void setReportingUser(Report_UserInfoDto value) {
        if (!Objects.equals(userReportDto.getReportingUser(), value))
            userReportDto.setReportingUser(value);

        notifyPropertyChanged(BR.reportingUser);
    }

    public UserReportBaseObservable(UserReportDto userReportDto) {
        this.userReportDto = userReportDto;
    }
}
