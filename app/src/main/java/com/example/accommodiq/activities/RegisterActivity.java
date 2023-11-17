package com.example.accommodiq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.accommodiq.R;
import com.example.accommodiq.Utility.TextUtilities;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView alreadyHaveAccountBtn = findViewById(R.id.alreadyHaveAccount);
        alreadyHaveAccountBtn.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));

        TextUtilities.makeTextUnderlined(alreadyHaveAccountBtn);
    }
}