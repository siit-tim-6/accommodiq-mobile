package com.example.accommodiq.ui.account;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.fragments.FragmentTransition;

public class MyProfileFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity mainActivity = getActivity();
        if (mainActivity != null) {
            if (!JwtUtils.isUserLoggedIn(getContext())) {
                Navigation.findNavController(view).navigate(R.id.action_myProfileFragment_to_unauthorizedProfileFragment);
            } else {
                Navigation.findNavController(view).navigate(R.id.action_myProfileFragment_to_profileFragment);
            }
        }
    }
}
