package com.example.accommodiq.dtos;

import com.example.accommodiq.enums.AccountRole;

public class AccountDetailsDto {
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private AccountRole role;

    public AccountDetailsDto() {
    }

    public AccountDetailsDto(String email, String firstName, String lastName, String address, String phoneNumber, AccountRole role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public AccountRole getRole() {
        return role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
