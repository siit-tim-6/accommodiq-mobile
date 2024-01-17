package com.example.accommodiq.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.JwtUtils;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    int SPLASH_TIME_OUT=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(isConnectedToInternet()){
            navigateToWelcomeScreenAfterDelay(SPLASH_TIME_OUT);
        }else {
            openWifiSettings();
        }
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

        return networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
    }

    private void navigateToWelcomeScreenAfterDelay(int delayMilliseconds) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (JwtUtils.isUserLoggedIn(getApplicationContext()) && !JwtUtils.isTokenExpired(getApplicationContext())) {
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            } else {
                navigateToLogin();
            }
        }, delayMilliseconds);
    }

    private void openWifiSettings() {
        ActivityResultLauncher<Intent> networkSettingsLauncher = registerForNetworkSettings();
        Toast.makeText(this, "Device is not connected to the internet.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        networkSettingsLauncher.launch(intent);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private ActivityResultLauncher<Intent> registerForNetworkSettings() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (isConnectedToInternet()) {
                navigateToLogin();Toast.makeText(this, "Device is connected to the internet successfully.", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            } else {
                showNoInternetMessageAndExit();
            }
        });
    }

    private void showNoInternetMessageAndExit() {
        Toast.makeText(this, "Device is still not connected to the internet. The App will be closed soon!", Toast.LENGTH_SHORT).show();
        finish();
    }
}