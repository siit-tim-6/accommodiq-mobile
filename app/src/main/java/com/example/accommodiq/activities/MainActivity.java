package com.example.accommodiq.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.accommodiq.R;
import com.example.accommodiq.databinding.ActivityMainBinding;
import com.example.accommodiq.models.Account;
import com.example.accommodiq.services.AccountService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.accommodiq.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Account loggedInAccount = AccountService.getInstance().getLoggedInAccount();
        if (loggedInAccount == null) {
            binding.navView.getMenu().clear();
            binding.navView.inflateMenu(R.menu.bottom_nav_menu_unauthorized);
        }
        else {
            switch (loggedInAccount.getAccountType()) {
                case OWNER:
                    binding.navView.getMenu().clear();
                    binding.navView.inflateMenu(R.menu.bottom_nav_menu_owner);
                    break;
                case ADMIN:
                    binding.navView.getMenu().clear();
                    binding.navView.inflateMenu(R.menu.bottom_nav_menu_admin);
                    break;
            }
        }
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}