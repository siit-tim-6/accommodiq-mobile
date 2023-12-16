package com.example.accommodiq.ui.newAccommodation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.AvailabilityRangeListAdapter;
import com.example.accommodiq.databinding.FragmentNewAccommodationBinding;
import com.example.accommodiq.dtos.AccommodationCreateDto;
import com.example.accommodiq.models.Availability;
import com.google.android.material.datepicker.MaterialDatePicker;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
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
    private String selectedApartmentType;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;
    private List<Uri> uploadedImageUris = new ArrayList<>();

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
                        uploadedImageUris.clear(); // Clear previous selections
                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                uploadedImageUris.add(imageUri);
                            }
                            binding.textViewSelectedImages.setText("Selected Images: " + count);
                        } else if (data.getData() != null) {
                            Uri imageUri = data.getData();
                            uploadedImageUris.add(imageUri);
                            binding.textViewSelectedImages.setText("Selected Images: 1");
                        }
                    }
                }
        );

        binding.editTextSelectRange.setOnClickListener(v -> showMaterialDateRangePicker());
        binding.buttonUploadImages.setOnClickListener(v -> openGallery());
        binding.buttonAdd.setOnClickListener(v -> {
            addNewAvailability();
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.apartment_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAccommodationType.setAdapter(adapter);

        binding.spinnerAccommodationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedApartmentType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedApartmentType = null;
            }
        });

        binding.buttonCreate.setOnClickListener(v -> {
            if (validateInput()) {
                createAndSendAccommodationData();
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

    private AccommodationCreateDto createAccommodationFromInput() {
        String title = binding.editTextName.getText().toString();
        String description = binding.editTextDescription.getText().toString();
        String location = binding.editTextAddress.getText().toString();
        int minGuests = Integer.parseInt(binding.editTextMinGuests.getText().toString());
        int maxGuests = Integer.parseInt(binding.editTextMaxGuests.getText().toString());
        boolean automaticAcceptance = binding.automaticallyAcceptCB.isChecked();
        String pricingType = binding.pricePerGuestCB.isChecked() ? "PER_GUEST" : "PER_NIGHT";

        List<String> benefits = new ArrayList<>();
        if (binding.checkBoxAC.isChecked()) {
            benefits.add("Air Conditioning");
        }
        if (binding.checkBoxBreakfast.isChecked()) {
            benefits.add("Complimentary Breakfast");
        }
        if (binding.checkBoxKitchen.isChecked()) {
            benefits.add("Fully Equipped Kitchen");
        }
        if (binding.checkBoxBalcony.isChecked()) {
            benefits.add("Private Balcony");
        }

        AccommodationCreateDto newAccommodationDto = new AccommodationCreateDto();
        newAccommodationDto.setTitle(title);
        newAccommodationDto.setDescription(description);
        newAccommodationDto.setLocation(location);
        newAccommodationDto.setMinGuests(minGuests);
        newAccommodationDto.setMaxGuests(maxGuests);
        newAccommodationDto.setAutomaticAcceptance(automaticAcceptance);
        newAccommodationDto.setPricingType(pricingType);
        newAccommodationDto.setBenefits(benefits);
        newAccommodationDto.setType(selectedApartmentType);
        newAccommodationDto.setAvailable(adapter.getAvailabilityDtoList());

        return newAccommodationDto;
    }

    private void createAndSendAccommodationData() {
        newAccommodationViewModel.uploadImages(uploadedImageUris, getContext(), new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> uploadedImageUrls = response.body();
                    AccommodationCreateDto dto = createAccommodationFromInput();
                    dto.setImages(uploadedImageUrls);

                    newAccommodationViewModel.createNewAccommodation(dto, new Callback<AccommodationDetailsDto>() {
                        @Override
                        public void onResponse(Call<AccommodationDetailsDto> call, Response<AccommodationDetailsDto> response) {
                            if (response.isSuccessful()) {
                                // Accommodation creation successful
                                AccommodationDetailsDto accommodationDetails = response.body();
                                // TODO: Handle the successful creation of accommodation
                            } else {
                                // Handle the error
                                Toast.makeText(getContext(), "Failed to create accommodation", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AccommodationDetailsDto> call, Throwable t) {
                            // Handle the network or other errors here
                            Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Handle failure in image upload
                    Toast.makeText(getContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                // Handle failure in image upload
                Toast.makeText(getContext(), "Image upload error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean validateInput() {
        if (binding.editTextName.getText().toString().isEmpty() ||
                binding.editTextDescription.getText().toString().isEmpty() ||
                binding.editTextAddress.getText().toString().isEmpty() ||
                binding.editTextMinGuests.getText().toString().isEmpty() ||
                binding.editTextMaxGuests.getText().toString().isEmpty() ||
                selectedApartmentType == null ||
                newAccommodationViewModel.getAvailabilityListLiveData().getValue().isEmpty()) {
            Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private List<MultipartBody.Part> getMultipartBodyPartsFromUris(List<Uri> imageUris, Context context) {
        List<MultipartBody.Part> parts = new ArrayList<>();

        for (Uri imageUri : imageUris) {
            String filePath = getPathFromUri(context, imageUri);
            if (filePath != null) {
                File file = new File(filePath);

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

                MultipartBody.Part body = MultipartBody.Part.createFormData("images", file.getName(), requestFile);

                parts.add(body);
            }
        }

        return parts;
    }

    private String getPathFromUri(Context context, Uri uri) {
        String result = null;
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                result = cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }
}