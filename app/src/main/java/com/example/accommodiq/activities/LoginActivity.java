package com.example.accommodiq.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.accommodiq.R;
import com.example.accommodiq.Utility.TextUtilities;
import com.example.accommodiq.databinding.ActivityLoginBinding;
import com.example.accommodiq.dtos.LoginResponseDto;
import com.example.accommodiq.services.AccountService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpCreateAccountTextView();
        setUpCloseBtn();
    }

    public void logIn(View view) {
        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();

        AccountService.getInstance().logIn(email, password, new Callback<LoginResponseDto>() {
            @Override
            public void onResponse(Call<LoginResponseDto> call, Response<LoginResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Store JWT and user role
                    String jwt = response.body().getJwt();
                    // Save JWT in SharedPreferences or another secure place
                    // Navigate to MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    binding.inputPassword.setError("Invalid credentials");
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDto> call, Throwable t) {
                // Handle network error
            }
        });
    }

    private void setUpCreateAccountTextView() {
        TextView createAccountTextView = findViewById(R.id.createAccountTextView);
        createAccountTextView.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        TextUtilities.makeTextUnderlined(createAccountTextView);
    }

    private void setUpCloseBtn() {
        ImageView closeBtn = findViewById(R.id.closeButton);
        closeBtn.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
    }
}