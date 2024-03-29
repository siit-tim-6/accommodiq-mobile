package com.example.accommodiq.ui.newAccommodation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.Navigation;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.databinding.FragmentNewAccommodationBinding;
import com.example.accommodiq.dtos.AccommodationDetailsDto;
import com.example.accommodiq.dtos.LocationDto;
import com.example.accommodiq.dtos.ModifyAccommodationDto;

import java.io.IOException;
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
    private Geocoder geocoder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewAccommodationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
                accommodationDetailsDto = (ModifyAccommodationDto) args.getSerializable("accommodationToModify");
        }

        if (accommodationDetailsDto != null) {
            getImagesUris(accommodationDetailsDto.getImages());
        }

        newAccommodationViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new NewAccommodationViewModel(getContext(), accommodationDetailsDto);
            }
        }).get(NewAccommodationViewModel.class);

        geocoder = new Geocoder(requireContext());

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

    @SuppressLint("SetTextI18n")
    private void getImagesUris(List<String> images) {
        List<Uri> imageUris = new ArrayList<>();
        for (String image : images) {
            imageUris.add(Uri.parse(RetrofitClientInstance.getServerIp(requireContext()) + "/images/" + image));
        }
        binding.textViewSelectedImages.setText("Selected Images: " + imageUris.size());
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

        LocationDto locationDto = getLocationObject(location);

        ModifyAccommodationDto newAccommodationDto = new ModifyAccommodationDto();
        newAccommodationDto.setTitle(title);
        newAccommodationDto.setDescription(description);
        newAccommodationDto.setLocation(locationDto);
        newAccommodationDto.setMinGuests(minGuests);
        newAccommodationDto.setMaxGuests(maxGuests);
        newAccommodationDto.setAutomaticAcceptance(automaticAcceptance);
        newAccommodationDto.setBenefits(benefits);
        newAccommodationDto.setType(selectedApartmentType);
        if (accommodationDetailsDto != null) {
            newAccommodationDto.setId(accommodationDetailsDto.getId());
        }
        return newAccommodationDto;
    }

    private void createAndSendAccommodationData() {
        if (this.accommodationDetailsDto != null && this.uploadedImageUris.isEmpty()) {
            sendWithoutImages();
        }
        else {
            sendWithImages();
        }
    }

    private void sendWithoutImages() {
        ModifyAccommodationDto dto = createAccommodationFromInput();
        dto.setImages(accommodationDetailsDto.getImages());
        newAccommodationViewModel.createNewAccommodation(dto, new Callback<AccommodationDetailsDto>() {
            @Override
            public void onResponse(@NonNull Call<AccommodationDetailsDto> call, @NonNull Response<AccommodationDetailsDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).navigate(R.id.action_navigation_new_accommodation_to_navigation_host_accommodations);
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
    }

    private void sendWithImages() {
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
                                Navigation.findNavController(requireView()).navigate(R.id.action_navigation_new_accommodation_to_navigation_host_accommodations);
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
                Log.i("UPLOAD", "onFailure: " + t.getMessage());
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

        if (getLocationObject(binding.editTextAddress.getText().toString()) ==  null) {
            Toast.makeText(getContext(), "You have entered a non-existent address", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private LocationDto getLocationObject(String location) {
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                return new LocationDto(address.getAddressLine(0), address.getLatitude(), address.getLongitude());
            }
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

}