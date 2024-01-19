package com.example.accommodiq.apiConfig;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.accommodiq.activities.LoginActivity;

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

            String jwt = JwtUtils.getJwt(context);
            if (jwt != null) {
                original = original.newBuilder()
                        .header("Authorization", "Bearer " + jwt)
                        .method(original.method(), original.body())
                        .build();
            }

        Response response = chain.proceed(original);

        if (JwtUtils.isTokenExpired(context) && JwtUtils.isUserLoggedIn(context)) {
            redirectToLogin();
        }

        return response;
    }

    private void redirectToLogin() {
        JwtUtils.clearJwtAndRole(context);
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
