package com.example.accommodiq.dtos;

import com.example.accommodiq.models.User;

public class RegisterDto {
    private String email;
    private String password;
    private User user;
    private String role;

    // Constructor
    public RegisterDto(String email, String password, User user, String role) {
        this.email = email;
        this.password = password;
        this.user = user;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User getUser() {
        return user;
    }

    public String getRole() {
        return role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
