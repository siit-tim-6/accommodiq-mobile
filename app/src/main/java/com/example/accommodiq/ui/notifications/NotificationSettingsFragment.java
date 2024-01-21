package com.example.accommodiq.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.navigation.Navigation;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.NotificationSettingsListAdapter;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.NotificationsClient;
import com.example.accommodiq.databinding.FragmentNotificationSettingListBinding;
import com.example.accommodiq.dtos.NotificationSettingDto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationSettingsFragment extends ListFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ArrayList<NotificationSettingDto> notificationSettings = new ArrayList<>();
        NotificationSettingsListAdapter adapter = new NotificationSettingsListAdapter(getContext(), notificationSettings);
        setListAdapter(adapter);

        View view = inflater.inflate(R.layout.fragment_notification_setting_list, container, false);
        FragmentNotificationSettingListBinding binding = FragmentNotificationSettingListBinding.bind(view);

        binding.buttonSaveNotificationSettings.setOnClickListener(v -> {
            NotificationsClient client = RetrofitClientInstance.getRetrofitInstance(getContext()).create(NotificationsClient.class);
            Call<Void> call = client.updateNotificationSettings(notificationSettings);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), "Error " + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Navigation.findNavController(requireView()).popBackStack();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
        });
        return binding.getRoot();
    }
}
