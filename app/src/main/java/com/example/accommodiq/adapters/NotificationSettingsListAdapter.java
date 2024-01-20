package com.example.accommodiq.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.accommodiq.databinding.NotificationSettingCardBinding;
import com.example.accommodiq.dtos.NotificationSettingDto;
import com.example.accommodiq.ui.notifications.NotificationSettingBaseObservable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationSettingsListAdapter extends ArrayAdapter<NotificationSettingDto> {
    private final Context context;
    private final ArrayList<NotificationSettingDto> notificationSettings;
    private final NotificationsClient client;

    public NotificationSettingsListAdapter(Context context, ArrayList<NotificationSettingDto> notificationSettings) {
        super(context, R.layout.fragment_notification_setting_list, notificationSettings);
        this.client = RetrofitClientInstance.getRetrofitInstance(context).create(NotificationsClient.class);
        this.context = context;
        this.notificationSettings = notificationSettings;
        this.fetchNotifications();
    }

    private void fetchNotifications() {
        Call<List<NotificationSettingDto>> call = client.getNotificationSettings();

        call.enqueue(new Callback<List<NotificationSettingDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<NotificationSettingDto>> call, @NonNull Response<List<NotificationSettingDto>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                assert response.body() != null;
                notificationSettings.addAll(response.body());
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<NotificationSettingDto>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    @Override
    public int getCount() {
        return notificationSettings.size();
    }

    @Override
    public NotificationSettingDto getItem(int position) {
        return notificationSettings.get(position);
    }

    @Override
    public void remove(@Nullable NotificationSettingDto object) {
        notificationSettings.remove(object);
        notifyDataSetChanged();
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NotificationSettingDto notification = notificationSettings.get(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.notification_setting_card, parent, false);

        @NonNull NotificationSettingCardBinding binding = NotificationSettingCardBinding.bind(convertView);
        binding.setObservable(new NotificationSettingBaseObservable(notification));

        return binding.getRoot();
    }
}
