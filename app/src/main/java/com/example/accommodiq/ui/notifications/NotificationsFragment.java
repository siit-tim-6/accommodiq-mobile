package com.example.accommodiq.ui.notifications;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.navigation.Navigation;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.NotificationsListAdapter;
import com.example.accommodiq.databinding.FragmentNotificationListBinding;

import java.util.ArrayList;

public class NotificationsFragment extends ListFragment implements SensorEventListener {
    private SensorManager sensorManager;
    private NotificationsListAdapter adapter;
    public static final int SENSOR_SENSITIVITY = 4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adapter = new NotificationsListAdapter(getContext(), new ArrayList<>());
        setListAdapter(adapter);
        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        FragmentNotificationListBinding binding = FragmentNotificationListBinding.bind(view);
        binding.notificationSettingsButton.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.navigation_notification_settings));
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor == null) {
            Toast.makeText(requireContext(), "No proximity sensor found in device.", Toast.LENGTH_SHORT).show();
        } else {
            // registering our sensor with sensor manager
            sensorManager.registerListener(this,
                    proximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            Toast.makeText(requireContext(), "Proximity sensor changed", Toast.LENGTH_SHORT).show();
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //near
                Toast.makeText(requireContext(), "near", Toast.LENGTH_SHORT).show();
            } else {
                //far
                Toast.makeText(requireContext(), "far", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleProximity(SensorEvent sensorEvent) {
        float distance = sensorEvent.values[0];
        Toast.makeText(getContext(), "Distance: " + distance, Toast.LENGTH_SHORT).show();

        // You can adjust the proximity threshold based on your requirements
        if (distance < sensorEvent.sensor.getMaximumRange()) {
            // Proximity sensor is near
            Toast.makeText(getContext(), "Proximity sensor near", Toast.LENGTH_SHORT).show();
        } else {
            // Proximity sensor is far
            Toast.makeText(getContext(), "Proximity sensor far", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}