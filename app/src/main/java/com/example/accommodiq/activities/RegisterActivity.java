package com.example.accommodiq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.accommodiq.R;
import com.example.accommodiq.Utility.TextUtilities;
import com.example.accommodiq.databinding.ActivityRegisterBinding;
import com.example.accommodiq.models.Account;
import com.example.accommodiq.services.AccountService;

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
        closeBtn.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, MainActivity.class)));
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
        Account.AccountType role = binding.guestRadioBtn.isChecked() ? Account.AccountType.GUEST : Account.AccountType.OWNER;

        Account newAccount;
        try {
            newAccount = new Account(email, password, firstName, lastName, address, phoneNumber, role);
        } catch (IllegalArgumentException e) {
            binding.inputEmail.setError("Email cant be empty");
            return;
        }
        if (AccountService.getInstance().register(newAccount)) {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            binding.inputEmail.setError("Email already in use");
        }
    }
}