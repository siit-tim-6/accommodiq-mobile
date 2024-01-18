package com.example.accommodiq.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.NotificationsListAdapter;

import java.util.ArrayList;

public class NotificationsFragment extends ListFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NotificationsListAdapter adapter = new NotificationsListAdapter(getContext(), new ArrayList<>());
        setListAdapter(adapter);
        return inflater.inflate(R.layout.fragment_notification_list, container, false);
    }
}