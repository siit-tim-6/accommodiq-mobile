package com.example.accommodiq.listener;

import com.example.accommodiq.models.Availability;

public interface AvailabilityActionsListener {
    void onAddAvailability(Availability availability);
    void onRemoveAvailability(Long availabilityId);
}
