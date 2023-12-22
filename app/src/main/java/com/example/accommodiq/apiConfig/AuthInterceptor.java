package com.example.accommodiq.apiConfig;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        // Get saved JWT
        String jwt = JwtUtils.getJwt(context);
        if (jwt == null) return chain.proceed(original);

        // Add JWT to request header
        Request request = original.newBuilder()
                .header("Authorization", "Bearer " + jwt)
                .method(original.method(), original.body())
                .build();

        return chain.proceed(request);
    }
}
