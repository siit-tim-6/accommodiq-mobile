package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.dtos.CredentialsDto;
import com.example.accommodiq.dtos.LoginResponseDto;
import com.example.accommodiq.dtos.PasswordDto;
import com.example.accommodiq.dtos.RegisterDto;
import com.example.accommodiq.models.Account;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AccountApiService {
    @POST("/users")
    Call<RegisterDto> registerUser(@Body RegisterDto registerDto);

    @POST("/sessions")
    Call<LoginResponseDto> login(@Body CredentialsDto credentials);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET("users/me")
    Call<Account> getAccountDetails();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @PUT("users")
    Call<Void> updateAccount(@Body Account account);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @PUT("users/password")
    Call<Void> changePassword(@Body PasswordDto passwordDto);

    @DELETE("users")
    Call<Void> deleteAccount();
}
