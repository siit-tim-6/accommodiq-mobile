package com.example.accommodiq.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.accommodiq.activities.LoginActivity;
import com.example.accommodiq.databinding.FragmentAuthorizedProfileBinding;
import com.example.accommodiq.services.AccountService;

public class AuthorizedProfileFragment extends Fragment {
    private FragmentAuthorizedProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getAuthorizedUserView(inflater, container);
    }

    private View getAuthorizedUserView(@NonNull LayoutInflater inflater, ViewGroup container) {
        MyProfileViewModel myProfileViewModel =
                new ViewModelProvider(this).get(MyProfileViewModel.class);

        binding = FragmentAuthorizedProfileBinding.inflate(inflater, container, false);

        binding.signOutBtn.setOnClickListener(view -> {
            Activity mainActivity = getActivity();
            if (mainActivity != null) {
                startActivity(new Intent(mainActivity, LoginActivity.class));
                mainActivity.finish();
                AccountService.getInstance().signOut();
            }
        });

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