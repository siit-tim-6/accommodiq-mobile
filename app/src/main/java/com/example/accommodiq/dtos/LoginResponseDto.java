package com.example.accommodiq.dtos;

public class LoginResponseDto {
    String role;
    String jwt;

    public LoginResponseDto(String role, String jwt) {
        this.role = role;
        this.jwt = jwt;
    }

    public String getRole() {
        return role;
    }

    public String getJwt() {
        return jwt;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
