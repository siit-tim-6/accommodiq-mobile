package com.example.accommodiq.ui.newAccommodation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.accommodiq.R;
import com.example.accommodiq.databinding.FragmentNewAccommodationBinding;
import com.example.accommodiq.dtos.AccommodationDetailsDto;
import com.example.accommodiq.dtos.ModifyAccommodationDto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewAccommodationFragment extends Fragment {

    private NewAccommodationViewModel newAccommodationViewModel;
    private FragmentNewAccommodationBinding binding;
    private String selectedApartmentType;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;
    private final List<Uri> uploadedImageUris = new ArrayList<>();
    private ModifyAccommodationDto accommodationDetailsDto;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewAccommodationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
                accommodationDetailsDto = (ModifyAccommodationDto) args.getSerializable("accommodationToModify");
        }

        newAccommodationViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new NewAccommodationViewModel(getContext(), accommodationDetailsDto);
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

        if (accommodationDetailsDto != null) {
            binding.modifyAccommodationTextView.setText("Update Accommodation");
            binding.buttonModify.setText("Update");
            setBenefits();
            populateFields();
        }

        binding.buttonUploadImages.setOnClickListener(v ->
                openGallery());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.accommodation_types, android.R.layout.simple_spinner_item);
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

        binding.buttonModify.setOnClickListener(v -> {
            if (validateInput()) {
                createAndSendAccommodationData();
            }
        });
    }

    private void populateFields() {
        binding.editTextName.setText(accommodationDetailsDto.getTitle());
        binding.editTextDescription.setText(accommodationDetailsDto.getDescription());
        binding.editTextAddress.setText(accommodationDetailsDto.getLocation().getAddress());
        binding.editTextMinGuests.setText(String.valueOf(accommodationDetailsDto.getMinGuests()));
        binding.editTextMaxGuests.setText(String.valueOf(accommodationDetailsDto.getMaxGuests()));
        binding.automaticallyAcceptCB.setChecked(accommodationDetailsDto.getAutomaticAcceptance());
    }

    private void setBenefits() {
        if (accommodationDetailsDto == null) return;
        binding.checkBoxAC.setChecked(accommodationDetailsDto.getBenefits().contains("Air Conditioning"));
        binding.checkBoxBreakfast.setChecked(accommodationDetailsDto.getBenefits().contains("Complimentary Breakfast"));
        binding.checkBoxKitchen.setChecked(accommodationDetailsDto.getBenefits().contains("Fully Equipped Kitchen"));
        binding.checkBoxBalcony.setChecked(accommodationDetailsDto.getBenefits().contains("Private Balcony"));
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

    private ModifyAccommodationDto createAccommodationFromInput() {
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

        ModifyAccommodationDto newAccommodationDto = new ModifyAccommodationDto();
        newAccommodationDto.setTitle(title);
        newAccommodationDto.setDescription(description);
        newAccommodationDto.setLocation(null); // TODO: Add location
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
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> uploadedImageUrls = response.body();
                    ModifyAccommodationDto dto = createAccommodationFromInput();
                    dto.setImages(uploadedImageUrls);

                    newAccommodationViewModel.createNewAccommodation(dto, new Callback<AccommodationDetailsDto>() {
                        @Override
                        public void onResponse(@NonNull Call<AccommodationDetailsDto> call, @NonNull Response<AccommodationDetailsDto> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                if (accommodationDetailsDto == null) resetFields();
                            } else {
                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<AccommodationDetailsDto> call, @NonNull Throwable t) {
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
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
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