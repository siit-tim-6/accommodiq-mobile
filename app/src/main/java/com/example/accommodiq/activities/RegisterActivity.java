package com.example.accommodiq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.accommodiq.R;
import com.example.accommodiq.Utility.TextUtilities;
import com.example.accommodiq.config.RetrofitClientInstance;
import com.example.accommodiq.databinding.ActivityRegisterBinding;
import com.example.accommodiq.dtos.RegisterDto;
import com.example.accommodiq.models.Account;
import com.example.accommodiq.models.User;
import com.example.accommodiq.services.AccountService;
import com.example.accommodiq.services.interfaces.AccountApiService;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
        closeBtn.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            AccountService.getInstance().signOut();
        });
    }

    public void register(View view) {
        // Check if account params are valid

        String firstName = binding.inputFirstName.getText().toString().trim();
        String lastName = binding.inputLastName.getText().toString().trim();
        String email = binding.inputEmail.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();
        String confirmPassword = binding.inputRepeatPassword.getText().toString().trim();
        String phoneNumber = binding.inputPhoneNumber.getText().toString().trim();
        String address = binding.inputAddress.getText().toString().trim();
        String role = "";
        role = binding.guestRadioBtn.isChecked() ? "GUEST";
        role = binding.ownerRadioButton.isChecked() ? "OWNER";

        // Perform input validation
        if (!validateInputs(firstName, lastName, email, password, confirmPassword, phoneNumber, address, role)) {
            return; // Stop if validation fails
        }

        // Prepare DTO
        User user = new User(firstName, lastName, phoneNumber, address);
        RegisterDto registerDto = new RegisterDto(email, password, user, role);

    }

    private boolean validateInputs(String firstName, String lastName, String email, String password, String confirmPassword, String phoneNumber, String address,String role) {
        // Check if fields are empty, passwords match, email is valid, etc.
        // Return true if validation passes, false otherwise
    }
}