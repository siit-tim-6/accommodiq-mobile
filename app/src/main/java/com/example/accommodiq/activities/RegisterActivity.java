package com.example.accommodiq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.accommodiq.R;
import com.example.accommodiq.Utility.TextUtilities;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setUpAlreadyHaveAccountBtn();
        setUpCloseBtn();
    }

    private void setUpAlreadyHaveAccountBtn() {
        TextView alreadyHaveAccountBtn = findViewById(R.id.alreadyHaveAccount);
        alreadyHaveAccountBtn.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
        TextUtilities.makeTextUnderlined(alreadyHaveAccountBtn);
    }

    private void setUpCloseBtn() {
        ImageView closeBtn = findViewById(R.id.closeButton);
        closeBtn.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, MainActivity.class)));
    }
}