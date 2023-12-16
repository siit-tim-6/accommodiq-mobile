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
    private List<Availability> availabilityList = new ArrayList<>();
    private Long selectedFromDate;
    private Long selectedToDate;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;
    private static final int REQUEST_GALLERY = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        newAccommodationViewModel = new ViewModelProvider(this).get(NewAccommodationViewModel.class);

        initSampleData();
        adapter = new AvailabilityRangeListAdapter(availabilityList);

        binding = FragmentNewAccommodationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AvailabilityRangeListAdapter adapter = new AvailabilityRangeListAdapter(availabilityList);
        binding.recyclerViewAvailabilityRange.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewAvailabilityRange.setAdapter(adapter);

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
                    adapter.addItem(newAvailability);
                } else {
                    Toast.makeText(getContext(), "Price must be greater than 0", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please select a date range first.", Toast.LENGTH_SHORT).show();
            }
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

    private void initSampleData() {
        // Sample date initialization
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date startDate1 = dateFormat.parse("03/12/2023");
            Date endDate1 = dateFormat.parse("05/12/2023");
            availabilityList.add(new Availability(1L, startDate1.getTime(), endDate1.getTime(), 100.00));

            Date startDate2 = dateFormat.parse("10/12/2023");
            Date endDate2 = dateFormat.parse("15/12/2023");
            availabilityList.add(new Availability(2L, startDate2.getTime(), endDate2.getTime(), 150.00));

            Date startDate3 = dateFormat.parse("12/12/2023");
            Date endDate3 = dateFormat.parse("20/12/2023");
            availabilityList.add(new Availability(3L, startDate3.getTime(), endDate3.getTime(), 250.00));
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the error according to your needs
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryActivityResultLauncher.launch(Intent.createChooser(intent, "Select Images"));
    }

}