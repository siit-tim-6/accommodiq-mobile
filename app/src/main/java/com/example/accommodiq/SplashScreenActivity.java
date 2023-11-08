package com.example.accommodiq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        int SPLASH_TIME_OUT=5000;

        if(isConnectedToInternet()){
            new Handler(Looper.getMainLooper()).postDelayed((Runnable) () -> navigateToWelcomeScreen(),SPLASH_TIME_OUT);
        }else {
            Toast.makeText(this, "Device is not connected to the internet.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isConnectedToInternet()) {
            navigateToWelcomeScreen();
        }else {
            Toast.makeText(this, "Device is still not connected to the internet.The App will be closed soon!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

        return networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
    }

    private void navigateToWelcomeScreen() {
        Intent intent = new Intent(SplashScreenActivity.this, WelcomeScreenActivity.class);
        startActivity(intent);
        finish();
    }
}