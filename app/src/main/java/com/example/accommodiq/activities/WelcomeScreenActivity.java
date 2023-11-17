package com.example.accommodiq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.accommodiq.R;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        TextView signUpBtn = findViewById(R.id.getStartedBtn);
        signUpBtn.setOnClickListener(view -> startActivity(new Intent(WelcomeScreenActivity.this, LoginActivity.class)));
    }
}