package com.example.accommodiq.clients;

import com.example.accommodiq.models.Account;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface AccountService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET("users/personal")
    Call<Account> getAccountDetails(@Header("Authorization") String token);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @PUT("users")
    Call<Void> updateAccount(@Header("Authorization") String token, @Body Account account);

}
