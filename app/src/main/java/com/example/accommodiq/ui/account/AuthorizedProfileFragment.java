package com.example.accommodiq.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.accommodiq.activities.LoginActivity;
import com.example.accommodiq.databinding.FragmentAuthorizedProfileBinding;
import com.example.accommodiq.services.AccountService;

public class AuthorizedProfileFragment extends Fragment {
    private FragmentAuthorizedProfileBinding binding;
    private AuthorizedProfileViewModel authorizedProfileViewModel;

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.INVISIBLE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getAuthorizedUserView(inflater, container);
    }

    private View getAuthorizedUserView(@NonNull LayoutInflater inflater, ViewGroup container) {
        authorizedProfileViewModel =
                new ViewModelProvider(this).get(AuthorizedProfileViewModel.class);

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
        binding.toggleButtonChangePassword.setOnClickListener(this::onToggleClicked);
        binding.confirmBtn.setOnClickListener((view)->{
            updateViewModel();
            authorizedProfileViewModel.updateAccountDetails(view);});
        collapse(binding.changePasswordContainer);

        updateViewModel();
        return root;
    }

    private void updateViewModel() {
        //        authorizedProfileViewModel.getEmailLiveData().observe(getViewLifecycleOwner(), binding.inputEmail::setText);
        //        authorizedProfileViewModel.getFirstNameLiveData().observe(getViewLifecycleOwner(), binding.inputFirstName::setText);
        //        authorizedProfileViewModel.getLastNameLiveData().observe(getViewLifecycleOwner(), binding.inputLastName::setText);
        //        authorizedProfileViewModel.getPasswordLiveData().observe(getViewLifecycleOwner(), binding.inputNewPassword::setText);
        //        authorizedProfileViewModel.getPhoneNumberLiveData().observe(getViewLifecycleOwner(), binding.inputPhoneNumber::setText);
        //        authorizedProfileViewModel.getAddressLiveData().observe(getViewLifecycleOwner(), binding.inputAddress::setText);
        //        authorizedProfileViewModel.getShowChangePasswordLiveData().observe(getViewLifecycleOwner(), binding.toggleButtonChangePassword::setChecked);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onToggleClicked(View view) {
        boolean isChecked = binding.toggleButtonChangePassword.isChecked();
        if (isChecked) {
            expand(binding.changePasswordContainer);
        } else {
            collapse(binding.changePasswordContainer);
        }
    }


}
