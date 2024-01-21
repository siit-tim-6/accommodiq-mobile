package com.example.accommodiq.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.NotificationsClient;
import com.example.accommodiq.databinding.NotificationCardBinding;
import com.example.accommodiq.dtos.NotificationDto;
import com.example.accommodiq.ui.notifications.NotificationBaseObservable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsListAdapter extends ArrayAdapter<NotificationDto> implements SensorEventListener {
    private final Context context;
    private final ArrayList<NotificationDto> notifications;
    private final NotificationsClient client;

    private final SensorManager sensorManager;
    private final Sensor proximitySensor;
    private final PowerManager.WakeLock wakeLock;
    private boolean isHandClose = false;  // Track hand proximity state


    public NotificationsListAdapter(Context context, ArrayList<NotificationDto> notifications) {
        super(context, R.layout.fragment_notification_list, notifications);
        this.client = RetrofitClientInstance.getRetrofitInstance(context).create(NotificationsClient.class);
        this.context = context;
        this.notifications = notifications;
        this.fetchNotifications();

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // Initialize wake lock
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, getClass().getSimpleName());

    }

    private void fetchNotifications() {
        Call<List<NotificationDto>> call = client.getNotifications();

        call.enqueue(new Callback<List<NotificationDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<NotificationDto>> call, @NonNull Response<List<NotificationDto>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                assert response.body() != null;
                notifications.addAll(new ArrayList<>(response.body()).stream().filter(notification -> !notification.isSeen()).collect(Collectors.toList()));
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<NotificationDto>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public NotificationDto getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public void remove(@Nullable NotificationDto object) {
        notifications.remove(object);
        notifyDataSetChanged();
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NotificationDto notification = notifications.get(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.notification_card, parent, false);
        @NonNull NotificationCardBinding binding = NotificationCardBinding.bind(convertView);
        binding.setObservable(new NotificationBaseObservable(notification));

        binding.seenButton.setOnClickListener(v -> {
            Call<Void> call = client.seenNotification(notification.getId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "Error " + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    remove(notification);
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
        });
        
        return binding.getRoot();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float distance = event.values[0];
            if (distance >= 0 && distance < proximitySensor.getMaximumRange()) {
                // User's hand is close to the sensor
                if (!isHandClose) {
                    isHandClose = true;
                    wakeLock.acquire(10*60*1000L /*10 minutes*/);
                    Toast.makeText(context, "Hand is close", Toast.LENGTH_SHORT).show();
//                    seenAllNotifications();  // Call API to mark all notifications as seen
                }
            } else {
                // User's hand is far from the sensor
                if (isHandClose) {
                    isHandClose = false;
                    wakeLock.release();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Not used
    }

    private void seenAllNotifications() {
        Call<Void> call = client.seenAllNotifications();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                // Handle successful response as needed
             fetchNotifications();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
            }
        });
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        sensorManager.unregisterListener(this);
    }
}
