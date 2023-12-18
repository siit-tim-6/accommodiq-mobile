package com.example.accommodiq.ui.updateAccommodationAvailability;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.AvailabilityRangeListAdapter;
import com.example.accommodiq.databinding.FragmentUpdateAccommodationAvailabilityBinding;
import com.example.accommodiq.dtos.AccommodationBookingDetailsDto;
import com.example.accommodiq.dtos.AccommodationDetailsDto;
import com.example.accommodiq.dtos.AvailabilityDto;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.listener.AvailabilityActionsListener;
import com.example.accommodiq.models.Availability;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAccommodationAvailability extends Fragment implements AvailabilityActionsListener {

    // Assuming accommodationId is obtained somehow (e.g., passed via Bundle or ViewModel)
    Long accommodationId = 1L; // Replace with actual accommodation ID
    private UpdateAccommodationAvailabilityViewModel mViewModel;
    private RecyclerView recyclerView;
    private AvailabilityRangeListAdapter adapter;
    private FragmentUpdateAccommodationAvailabilityBinding binding; // Binding object

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateAccommodationAvailabilityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(UpdateAccommodationAvailabilityViewModel.class);

        List<Availability> availabilityList = mViewModel.getAvailabilityListLiveData().getValue();

        binding.recyclerViewAvailabilityRange.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AvailabilityRangeListAdapter(availabilityList, this );
        binding.recyclerViewAvailabilityRange.setAdapter(adapter);

        binding.buttonUpdate.setOnClickListener(v -> {
            String cancellationDeadline = binding.editTextCancellationDeadline.getText().toString().trim();
            boolean pricePerGuest = binding.checkBoxPricePerGuest.isChecked();

            AccommodationBookingDetailsDto dto = new AccommodationBookingDetailsDto();
            dto.setCancellationDeadline(Integer.parseInt(cancellationDeadline));
            dto.setPricingType(pricePerGuest? "PER_GUEST" : "PER_NIGHT");

            mViewModel.updateAccommodationBookingDetails(accommodationId, dto, new Callback<AccommodationDetailsDto>() {
                @Override
                public void onResponse(Call<AccommodationDetailsDto> call, Response<AccommodationDetailsDto> response) {
                    if (response.isSuccessful()) {
                        // Handle success - Update UI or show a success message
                    } else {
                        // Handle error - Show error message
                    }
                }

                @Override
                public void onFailure(Call<AccommodationDetailsDto> call, Throwable t) {
                    // Handle network error
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leak
    }

    @Override
    public void onAddAvailability(Availability availability) {
        AvailabilityDto dto = new AvailabilityDto(availability);
        mViewModel.addAccommodationAvailability(accommodationId, dto, new Callback<List<Availability>>() {
            @Override
            public void onResponse(Call<List<Availability>> call, Response<List<Availability>> response) {
                if (response.isSuccessful()) {
                    // Update UI with the new list of availabilities
                    adapter.setAvailabilityRangeList(response.body());
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<List<Availability>> call, Throwable t) {
                // Handle network error
            }
        });
    }

    @Override
    public void onRemoveAvailability(Long availabilityId) {
        mViewModel.removeAccommodationAvailability(accommodationId, availabilityId, new Callback<MessageDto>() {
            @Override
            public void onResponse(Call<MessageDto> call, Response<MessageDto> response) {
                if (response.isSuccessful()) {
                    // Remove the availability from the UI
                    adapter.removeItemByAvailabilityId(availabilityId);
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<MessageDto> call, Throwable t) {
                // Handle network error
            }
        });
    }
}