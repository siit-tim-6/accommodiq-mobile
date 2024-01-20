package com.example.accommodiq.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.databinding.FragmentSearchBinding;
import com.example.accommodiq.enums.AccommodationListType;

import java.util.Objects;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSearchBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Activity mainActivity = getActivity();
        if (mainActivity != null) {
            if (Objects.equals(JwtUtils.getRole(mainActivity), "ADMIN")) {
                Navigation.findNavController(requireView()).navigate(R.id.navigation_user_reports);
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable("accommodationListType", AccommodationListType.SEARCH);
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_search_to_accommodationsListFragment, bundle);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}