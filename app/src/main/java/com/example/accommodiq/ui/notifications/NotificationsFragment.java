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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.navigation.Navigation;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.NotificationsListAdapter;
import com.example.accommodiq.databinding.FragmentNotificationListBinding;

import java.util.ArrayList;

public class NotificationsFragment extends ListFragment implements SensorEventListener {
    private static final int SENSOR_SENSITIVITY = 4;
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private NotificationsListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adapter = new NotificationsListAdapter(getContext(), new ArrayList<>());
        setListAdapter(adapter);
        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        FragmentNotificationListBinding binding = FragmentNotificationListBinding.bind(view);
        binding.notificationSettingsButton.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.navigation_notification_settings));

        mSensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                adapter.seenAllNotifications();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}