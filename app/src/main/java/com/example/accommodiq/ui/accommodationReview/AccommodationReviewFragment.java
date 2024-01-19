package com.example.accommodiq.ui.accommodationReview;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.accommodiq.R;
import com.example.accommodiq.databinding.FragmentAccommodationReviewBinding;
import com.example.accommodiq.enums.AccommodationListType;
import com.example.accommodiq.fragments.AccommodationsListFragment;
import com.example.accommodiq.fragments.FragmentTransition;

public class AccommodationReviewFragment extends Fragment {
    private FragmentAccommodationReviewBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccommodationReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Activity mainActivity = getActivity();
        if (mainActivity != null) {
            //FragmentTransition.to(AccommodationsListFragment.newInstance(getActivity(), AccommodationListType.ADMIN_REVIEW_PENDING_ACCOMMODATIONS), getActivity(), true, R.id.admin_accommodation_review_fragment);
            Bundle bundle = new Bundle();
            bundle.putSerializable("accommodationListType", AccommodationListType.ADMIN_REVIEW_PENDING_ACCOMMODATIONS);
            Navigation.findNavController(requireView()).navigate(R.id.action_accommodationReview_to_accommodationsListFragment, bundle);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
