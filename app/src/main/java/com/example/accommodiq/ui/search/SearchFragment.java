package com.example.accommodiq.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.accommodiq.R;
import com.example.accommodiq.databinding.FragmentSearchBinding;
import com.example.accommodiq.fragments.AccommodationsListFragment;
import com.example.accommodiq.fragments.FragmentTransition;
import com.example.accommodiq.models.Accommodation;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private ArrayList<Accommodation> accommodations = new ArrayList<>();
    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        prepareAccommodationList(accommodations);
        Activity mainActivity = getActivity();
        if (mainActivity != null) {
            FragmentTransition.to(AccommodationsListFragment.newInstance(accommodations, false), getActivity(), false, R.id.accommodations_fragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareAccommodationList(ArrayList<Accommodation> accommodations) {
        accommodations.clear();
        accommodations.add(new Accommodation(1, "City Center Apartment", "test description", 4.92, 390, "Mileticeva 23, 21000 Novi Sad, Serbia", 2, 4, R.drawable.accommodation_image, 250));
        accommodations.add(new Accommodation(2, "Petrovaradin Apartment", "test description", 5.00, 490, "Preradoviceva 23, 21000 Novi Sad, Serbia", 1, 3, R.drawable.accommodation_image, 350));
        accommodations.add(new Accommodation(3, "Detelinara Apartment", "test description", 3.90, 300, "Koste Racina 23, 21000 Novi Sad, Serbia", 1, 4, R.drawable.accommodation_image, 200));
        accommodations.add(new Accommodation(4, "Podbara Apartment", "test description", 4.52, 120, "Kosovska 23, 21000 Novi Sad, Serbia", 2, 5, R.drawable.accommodation_image, 100));
    }
}