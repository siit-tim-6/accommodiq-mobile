package com.example.accommodiq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accommodiq.R;
import com.example.accommodiq.utils.TextUtils;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.databinding.ActivityRegisterBinding;
import com.example.accommodiq.dtos.RegisterDto;
import com.example.accommodiq.models.User;
import com.example.accommodiq.services.interfaces.AccountApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    AccountApiService accountApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpAlreadyHaveAccountBtn();
        setUpCloseBtn();
        accountApiService = RetrofitClientInstance.getRetrofitInstance(this).create(AccountApiService.class);
    }

    private void setUpAlreadyHaveAccountBtn() {
        TextView alreadyHaveAccountBtn = findViewById(R.id.alreadyHaveAccount);
        alreadyHaveAccountBtn.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
        TextUtils.makeTextUnderlined(alreadyHaveAccountBtn);
    }

    private void setUpCloseBtn() {
        ImageView closeBtn = findViewById(R.id.closeButton);
        closeBtn.setOnClickListener(view -> {
            // Clear JWT and role
            JwtUtils.clearJwtAndRole(getApplicationContext());

            // Navigate back to MainActivity
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        });
    }

    public void register(View view) {
        String firstName = binding.inputFirstName.getText().toString().trim();
        String lastName = binding.inputLastName.getText().toString().trim();
        String email = binding.inputEmail.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();
        String confirmPassword = binding.inputRepeatPassword.getText().toString().trim();
        String phoneNumber = binding.inputPhoneNumber.getText().toString().trim();
        String address = binding.inputAddress.getText().toString().trim();
        String role = "";
        if (binding.guestRadioBtn.isChecked()) {
            role = "GUEST";
        } else if (binding.ownerRadioButton.isChecked()) {
            role = "OWNER";
        }

        if (!validateInputs(firstName, lastName, email, password, confirmPassword, phoneNumber, address, role)) {
            return;
        }

        User user = new User(firstName, lastName, phoneNumber, address);
        RegisterDto registerDto = new RegisterDto(email, password, user, role);

        Toast.makeText(RegisterActivity.this, "Registering...", Toast.LENGTH_SHORT).show();
        System.out.println(registerDto.toString());

        Call<RegisterDto> call = accountApiService.registerUser(registerDto);
        call.enqueue(new Callback<RegisterDto>() {
            @Override
            public void onResponse(Call<RegisterDto> call, Response<RegisterDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registration successful. Please verify your email.", Toast.LENGTH_LONG).show();
                    navigateToLoginActivity();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed. Please try again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterDto> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateInputs(String firstName, String lastName, String email, String password, String confirmPassword, String phoneNumber, String address,String role) {
        String emailRegex = "^[a-zA-Z0-9._-]+(\\+[a-zA-Z0-9._-]+)?@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";

        if (firstName.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "First name is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (lastName.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Last name is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty() || !email.matches(emailRegex)) {
            Toast.makeText(RegisterActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phoneNumber.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Phone number is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (address.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Address is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (role.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please select a role", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}