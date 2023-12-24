package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.dtos.CredentialsDto;
import com.example.accommodiq.dtos.LoginResponseDto;
import com.example.accommodiq.dtos.RegisterDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountApiService {
    @POST("/users")
    Call<RegisterDto> registerUser(@Body RegisterDto registerDto);

    @POST("/sessions")
    Call<LoginResponseDto> login(@Body CredentialsDto credentials);
}
