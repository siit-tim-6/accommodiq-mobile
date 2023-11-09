package com.example.accommodiq.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.accommodiq.databinding.FragmentMyProfileBinding;

public class MyProfileFragment extends Fragment {
    private FragmentMyProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyProfileViewModel myProfileViewModel =
                new ViewModelProvider(this).get(MyProfileViewModel.class);

        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        myProfileViewModel.getEmailLiveData().observe(getViewLifecycleOwner(), binding.inputEmail::setText);
        myProfileViewModel.getFirstNameLiveData().observe(getViewLifecycleOwner(), binding.inputFirstName::setText);
        myProfileViewModel.getLastNameLiveData().observe(getViewLifecycleOwner(), binding.inputLastName::setText);
        myProfileViewModel.getPasswordLiveData().observe(getViewLifecycleOwner(), binding.inputPassword::setText);
        myProfileViewModel.getPhoneNumberLiveData().observe(getViewLifecycleOwner(), binding.inputPhoneNumber::setText);
        myProfileViewModel.getAddressLiveData().observe(getViewLifecycleOwner(), binding.inputAddress::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
