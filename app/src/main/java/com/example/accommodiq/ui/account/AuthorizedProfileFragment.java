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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.accommodiq.activities.LoginActivity;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.databinding.FragmentAuthorizedProfileBinding;
import com.example.accommodiq.services.interfaces.AccountApiService;
import com.google.firebase.messaging.FirebaseMessaging;

public class AuthorizedProfileFragment extends Fragment {
    private FragmentAuthorizedProfileBinding binding;

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
        AccountApiService accountApiService = RetrofitClientInstance.getRetrofitInstance(getContext()).create(AccountApiService.class);

        binding = DataBindingUtil.inflate(inflater, com.example.accommodiq.R.layout.fragment_authorized_profile, container, false);
        binding.setViewModel(new AuthorizedProfileViewModel(accountApiService));
        View root = binding.getRoot();

        binding.signOutBtn.setOnClickListener(view -> {
            Activity mainActivity = getActivity();
            if (mainActivity != null) {
                startActivity(new Intent(mainActivity, LoginActivity.class));
                mainActivity.finish();
                FirebaseMessaging.getInstance().unsubscribeFromTopic("user-" + JwtUtils.getLoggedInId(requireContext()));
                JwtUtils.clearJwtAndRole(requireContext());

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.toggleButtonChangePassword.setOnClickListener(this::onToggleClicked);

        collapse(binding.changePasswordContainer);
        return root;
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
