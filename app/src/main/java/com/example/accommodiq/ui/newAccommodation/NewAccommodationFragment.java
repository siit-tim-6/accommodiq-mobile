package com.example.accommodiq.ui.newAccommodation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.accommodiq.adapters.AvailabilityRangeListAdapter;
import com.example.accommodiq.databinding.FragmentNewAccommodationBinding;
import com.example.accommodiq.dtos.AccommodationCreateDto;
import com.example.accommodiq.models.Accommodation;
import com.example.accommodiq.models.Availability;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewAccommodationFragment extends Fragment {

    private NewAccommodationViewModel newAccommodationViewModel;
    private FragmentNewAccommodationBinding binding;
    private AvailabilityRangeListAdapter adapter;
    private Long selectedFromDate;
    private Long selectedToDate;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewAccommodationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newAccommodationViewModel = new ViewModelProvider(this).get(NewAccommodationViewModel.class);

        adapter = new AvailabilityRangeListAdapter(new ArrayList<>());
        binding.recyclerViewAvailabilityRange.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewAvailabilityRange.setAdapter(adapter);

        newAccommodationViewModel.getAvailabilityListLiveData().observe(getViewLifecycleOwner(), availabilityList -> {
            adapter.setAvailabilityRangeList(availabilityList);
        });

        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            binding.textViewSelectedImages.setText("Selected Images: " + count);
                        } else if (data.getData() != null) {
                            binding.textViewSelectedImages.setText("Selected Images: 1");
                        }
                    }
                });

        binding.editTextSelectRange.setOnClickListener(v -> showMaterialDateRangePicker());
        binding.buttonUploadImages.setOnClickListener(v -> openGallery());
        binding.buttonAdd.setOnClickListener(v -> {
            addNewAvailability();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showMaterialDateRangePicker() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Date Range");
        final MaterialDatePicker<Pair<Long, Long>> picker = builder.build();

        picker.addOnPositiveButtonClickListener(selection -> {
            Pair<Long, Long> dateRange = selection;
            if (dateRange.first != null && dateRange.second != null) {
                selectedFromDate = dateRange.first;
                selectedToDate = dateRange.second;
                String fromDate = formatDate(dateRange.first);
                String toDate = formatDate(dateRange.second);
                binding.editTextSelectRange.setText(String.format("%s - %s", fromDate, toDate));
            }
        });

        picker.show(getChildFragmentManager(), picker.toString());
    }

    private String formatDate(Long dateInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date(dateInMillis));
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryActivityResultLauncher.launch(Intent.createChooser(intent, "Select Images"));
    }

    private void addNewAvailability() {
        if (selectedFromDate != null && selectedToDate != null) {
            String priceString = binding.editTextPrice.getText().toString();
            double price;
            try {
                price = Double.parseDouble(priceString);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid price entered", Toast.LENGTH_SHORT).show();
                return;
            }

            if (price > 0) {
                Availability newAvailability = new Availability(-1L, selectedFromDate, selectedToDate, price);
                newAccommodationViewModel.addAvailability(newAvailability);
            } else {
                Toast.makeText(getContext(), "Price must be greater than 0", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Please select a date range first.", Toast.LENGTH_SHORT).show();
        }
    }

    private void createAccommodationFromInput() {
        String title = binding.editTextName.getText().toString();
        String description = binding.editTextDescription.getText().toString();
        String location = binding.editTextAddress.getText().toString();
        int minGuests = Integer.parseInt(binding.editTextMinGuests.getText().toString());
        int maxGuests = Integer.parseInt(binding.editTextMaxGuests.getText().toString());
        // Collect other inputs...

        AccommodationCreateDto newAccommodationDto = new AccommodationCreateDto();
        newAccommodationDto.setTitle(title);
        newAccommodationDto.setDescription(description);
        newAccommodationDto.setLocation(location);
        newAccommodationDto.setMinGuests(minGuests);
        newAccommodationDto.setMaxGuests(maxGuests);
        // Set other fields...

        // Pass newAccommodationDto to ViewModel or directly to the API call method
    }

}