package com.example.accommodiq;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.accommodiq.Utility.TextUtilities;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView createAccountTextView = findViewById(R.id.createAccountTextView);
        createAccountTextView.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this,RegisterActivity.class)));

        TextUtilities.makeTextUnderlined(createAccountTextView);
    }
}