package com.example.accommodiq.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.accommodiq.activities.LoginActivity;
import com.example.accommodiq.databinding.FragmentUnauthorizedProfileBinding;

public class UnauthorizedProfileFragment extends Fragment {

    private FragmentUnauthorizedProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUnauthorizedProfileBinding.inflate(inflater, container, false);
        binding.registerBtn.setOnClickListener(this::logIn);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void logIn(View view) {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}
