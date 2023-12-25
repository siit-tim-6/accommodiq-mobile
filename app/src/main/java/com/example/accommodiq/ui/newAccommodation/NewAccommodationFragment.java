package com.example.accommodiq.ui.newAccommodation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModel;
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
import androidx.navigation.fragment.NavHostFragment;
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
import com.example.accommodiq.dtos.AccommodationDetailsDto;
import com.example.accommodiq.fragments.AccommodationDetailsFragment;
import com.example.accommodiq.fragments.AccommodationsListFragment;
import com.example.accommodiq.listener.AvailabilityActionsListener;
import com.example.accommodiq.models.Availability;
import com.example.accommodiq.ui.updateAccommodationAvailability.UpdateAccommodationAvailabilityViewModel;
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
    private String selectedApartmentType;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;
    private List<Uri> uploadedImageUris = new ArrayList<>();
    private AccommodationDetailsDto accommodationDetailsDto;

    public static NewAccommodationFragment newInstance(AccommodationDetailsDto accommodationDetailsDto) {
        NewAccommodationFragment fragment = new NewAccommodationFragment();
        fragment.accommodationDetailsDto = accommodationDetailsDto;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewAccommodationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newAccommodationViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new NewAccommodationViewModel(getContext());
            }
        }).get(NewAccommodationViewModel.class);

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

        binding.buttonUploadImages.setOnClickListener(v -> openGallery());

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

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryActivityResultLauncher.launch(Intent.createChooser(intent, "Select Images"));
    }

    private AccommodationCreateDto createAccommodationFromInput() {
        String title = binding.editTextName.getText().toString();
        String description = binding.editTextDescription.getText().toString();
        String location = binding.editTextAddress.getText().toString();
        int minGuests = Integer.parseInt(binding.editTextMinGuests.getText().toString());
        int maxGuests = Integer.parseInt(binding.editTextMaxGuests.getText().toString());
        boolean automaticAcceptance = binding.automaticallyAcceptCB.isChecked();

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
        newAccommodationDto.setBenefits(benefits);
        newAccommodationDto.setType(selectedApartmentType);

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
                                Toast.makeText(getContext(), "Successfully created!", Toast.LENGTH_SHORT).show();
                                // TODO: Handle the successful creation of accommodation
                                resetFields();
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
                selectedApartmentType == null) {
            Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void resetFields() {
        binding.editTextName.setText("");
        binding.editTextDescription.setText("");
        binding.editTextAddress.setText("");
        binding.editTextMinGuests.setText("");
        binding.editTextMaxGuests.setText("");
        binding.automaticallyAcceptCB.setChecked(false);

        // Reset the spinner to the default value
        binding.spinnerAccommodationType.setSelection(0);

        // Clear the checkboxes
        binding.checkBoxAC.setChecked(false);
        binding.checkBoxBreakfast.setChecked(false);
        binding.checkBoxKitchen.setChecked(false);
        binding.checkBoxBalcony.setChecked(false);

        // Clear the image URIs and update the display
        uploadedImageUris.clear();
        binding.textViewSelectedImages.setText("Selected Images: 0");
    }

}