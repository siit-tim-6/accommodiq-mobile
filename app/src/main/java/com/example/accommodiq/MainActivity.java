package com.example.accommodiq;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.accommodiq.fragments.AccommodationsListFragment;
import com.example.accommodiq.fragments.FragmentTransition;
import com.example.accommodiq.models.Accommodation;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Accommodation> accommodations = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareAccommodationList(accommodations);
        FragmentTransition.to(AccommodationsListFragment.newInstance(accommodations), MainActivity.this, false, R.id.fragment_main);
    }

    private void prepareAccommodationList(ArrayList<Accommodation> accommodations) {
        accommodations.add(new Accommodation(1, "City Center Apartment", "test description", 4.92, 390, "Mileticeva 23, 21000 Novi Sad, Serbia", 2, 4, R.drawable.accommodation_image, 250));
        accommodations.add(new Accommodation(2, "Petrovaradin Apartment", "test description", 5.00, 490, "Preradoviceva 23, 21000 Novi Sad, Serbia", 1, 3, R.drawable.accommodation_image, 350));
        accommodations.add(new Accommodation(3, "Detelinara Apartment", "test description", 3.90, 300, "Koste Racina 23, 21000 Novi Sad, Serbia", 1, 4, R.drawable.accommodation_image, 200));
        accommodations.add(new Accommodation(4, "Podbara Apartment", "test description", 4.52, 120, "Kosovska 23, 21000 Novi Sad, Serbia", 2, 5, R.drawable.accommodation_image, 100));
    }
}