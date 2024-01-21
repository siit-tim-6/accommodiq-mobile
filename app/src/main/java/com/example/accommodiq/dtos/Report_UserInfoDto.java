package com.example.accommodiq.dtos;

public class Report_UserInfoDto {
    long id;
    String name;
    String role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Report_UserInfoDto(long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
}
