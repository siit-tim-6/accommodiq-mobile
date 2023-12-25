package com.example.accommodiq.ui.account;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.fragments.FragmentTransition;

public class MyProfileFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Activity mainActivity = getActivity();
        if (mainActivity != null){
            if (!JwtUtils.isUserLoggedIn(getContext())) {
                FragmentTransition.to(new UnauthorizedProfileFragment(), getActivity(), true, R.id.my_profile_fragment);
            }
            else {
                FragmentTransition.to(new AuthorizedProfileFragment(), getActivity(), true, R.id.my_profile_fragment);
            }
        }
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }
}
