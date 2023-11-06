package com.example.accommodiq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView signUpBtn = findViewById(R.id.alreadyHaveAccount);
        signUpBtn.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this,LoginActivity.class)));
    }
}