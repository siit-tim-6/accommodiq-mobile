package com.example.accommodiq.ui.updateAccommodationAvailability;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.accommodiq.adapters.AvailabilityRangeListAdapter;
import com.example.accommodiq.databinding.FragmentUpdateAccommodationAvailabilityBinding;
import com.example.accommodiq.dtos.AccommodationBookingDetailFormDto;
import com.example.accommodiq.dtos.AccommodationBookingDetailsDto;
import com.example.accommodiq.dtos.AccommodationDetailsDto;
import com.example.accommodiq.dtos.AvailabilityDto;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.listener.AvailabilityActionsListener;
import com.example.accommodiq.models.Availability;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAccommodationAvailabilityFragment extends Fragment implements AvailabilityActionsListener {

    // Assuming accommodationId is obtained somehow (e.g., passed via Bundle or ViewModel)
    Long accommodationId=9L; // Replace with actual accommodation ID
    private UpdateAccommodationAvailabilityViewModel mViewModel;
    private AvailabilityRangeListAdapter adapter;
    private FragmentUpdateAccommodationAvailabilityBinding binding; // Binding object
    private Long selectedFromDate;
    private Long selectedToDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateAccommodationAvailabilityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new UpdateAccommodationAvailabilityViewModel(getContext());
            }
        }).get(UpdateAccommodationAvailabilityViewModel.class);

        Bundle args = getArguments();
        if (args != null) {
            accommodationId = args.getLong("accommodationId", -1L);
        }

        List<Availability> availabilityList = mViewModel.getAvailabilityListLiveData().getValue();

        binding.recyclerViewAvailabilityRange.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AvailabilityRangeListAdapter(availabilityList, this);
        binding.recyclerViewAvailabilityRange.setAdapter(adapter);
        binding.buttonAdd.setOnClickListener(v -> {
            addNewAvailability();
            selectedFromDate= null;
            selectedToDate = null;
            binding.editTextSelectRange.setText("");
            binding.editTextPrice.setText("");
        });
        binding.editTextSelectRange.setOnClickListener(v -> showMaterialDateRangePicker());
        binding.closePageButton.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        mViewModel.getAccommodationBookingDetails(accommodationId, new Callback<AccommodationBookingDetailFormDto>() {
            @Override
            public void onResponse(@NonNull Call<AccommodationBookingDetailFormDto> call, @NonNull Response<AccommodationBookingDetailFormDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccommodationBookingDetailFormDto bookingDetails = response.body();
                    populateBookingDetailsFields(bookingDetails);
                } else {
                    Toast.makeText(getContext(), "Failed to load booking details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccommodationBookingDetailFormDto> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        setupUpdateButton();
    }

    private void populateBookingDetailsFields(AccommodationBookingDetailFormDto bookingDetails) {
        binding.editTextCancellationDeadline.setText(String.valueOf(bookingDetails.getCancellationDeadline()));
        binding.checkBoxPricePerGuest.setChecked("PER_GUEST".equals(bookingDetails.getPricingType()));
        adapter.setAvailabilityRangeList(bookingDetails.getAvailable());
        adapter.notifyDataSetChanged();
    }

    private void showMaterialDateRangePicker() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Date Range");
        final MaterialDatePicker<Pair<Long, Long>> picker = builder.build();

        picker.addOnPositiveButtonClickListener(selection -> {
            if (selection.first != null && selection.second != null) {
                selectedFromDate = selection.first;
                selectedToDate = selection.second;
                String fromDate = formatDate(selection.first);
                String toDate = formatDate(selection.second);
                binding.editTextSelectRange.setText(String.format("%s - %s", fromDate, toDate));
            }
        });

        picker.show(getChildFragmentManager(), picker.toString());
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
                mViewModel.addAccommodationAvailability(accommodationId, new AvailabilityDto(newAvailability), new Callback<List<Availability>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Availability>> call, @NonNull Response<List<Availability>> response) {
                        if (response.isSuccessful()) {
                            adapter.setAvailabilityRangeList(response.body());
                            Toast.makeText(getContext(), "Availability added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to add availability", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Availability>> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Price must be greater than 0", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Please select a date range first.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupUpdateButton() {
        binding.buttonUpdate.setOnClickListener(v -> {
            String cancellationDeadline = binding.editTextCancellationDeadline.getText().toString().trim();
            boolean pricePerGuest = binding.checkBoxPricePerGuest.isChecked();

            AccommodationBookingDetailsDto dto = new AccommodationBookingDetailsDto();
            dto.setCancellationDeadline(Integer.parseInt(cancellationDeadline));
            dto.setPricingType(pricePerGuest ? "PER_GUEST" : "PER_NIGHT");

            mViewModel.updateAccommodationBookingDetails(accommodationId, dto, new Callback<AccommodationDetailsDto>() {
                @Override
                public void onResponse(@NonNull Call<AccommodationDetailsDto> call, @NonNull Response<AccommodationDetailsDto> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Booking details updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to update booking details", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccommodationDetailsDto> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String formatDate(Long dateInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date(dateInMillis));
    }

    @Override
    public void onRemoveAvailability(Availability availability) {
        mViewModel.removeAccommodationAvailability(accommodationId, availability.getId(), new Callback<MessageDto>() {
            @Override
            public void onResponse(@NonNull Call<MessageDto> call, @NonNull Response<MessageDto> response) {
                if (response.isSuccessful()) {
                    adapter.removeItemByAvailabilityId(availability.getId());
                    mViewModel.removeAvailabilityFromList(availability.getId());
                    Toast.makeText(getContext(), "Availability removed successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to remove availability", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageDto> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}