package com.example.accommodiq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView signUpBtn = findViewById(R.id.createAccountTextView);
        signUpBtn.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this,RegisterActivity.class)));
    }
}