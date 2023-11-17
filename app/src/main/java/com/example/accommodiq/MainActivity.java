package com.example.accommodiq;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.accommodiq.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.accommodiq.fragments.AccommodationsListFragment;
import com.example.accommodiq.fragments.FragmentTransition;
import com.example.accommodiq.models.Accommodation;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Accommodation> accommodations = new ArrayList<>();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_reservations, R.id.navigation_favorites, R.id.navigation_notifications, R.id.navigation_search, R.id.navigation_my_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
    
    private void prepareAccommodationList(ArrayList<Accommodation> accommodations) {
        accommodations.add(new Accommodation(1, "City Center Apartment", "test description", 4.92, 390, "Mileticeva 23, 21000 Novi Sad, Serbia", 2, 4, R.drawable.accommodation_image, 250));
        accommodations.add(new Accommodation(2, "Petrovaradin Apartment", "test description", 5.00, 490, "Preradoviceva 23, 21000 Novi Sad, Serbia", 1, 3, R.drawable.accommodation_image, 350));
        accommodations.add(new Accommodation(3, "Detelinara Apartment", "test description", 3.90, 300, "Koste Racina 23, 21000 Novi Sad, Serbia", 1, 4, R.drawable.accommodation_image, 200));
        accommodations.add(new Accommodation(4, "Podbara Apartment", "test description", 4.52, 120, "Kosovska 23, 21000 Novi Sad, Serbia", 2, 5, R.drawable.accommodation_image, 100));
    }

}