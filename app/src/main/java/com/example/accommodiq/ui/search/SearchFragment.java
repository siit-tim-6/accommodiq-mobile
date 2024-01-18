package com.example.accommodiq.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.accommodiq.R;
import com.example.accommodiq.databinding.FragmentSearchBinding;
import com.example.accommodiq.enums.AccommodationListType;
import com.example.accommodiq.fragments.AccommodationsListFragment;
import com.example.accommodiq.fragments.FragmentTransition;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Activity mainActivity = getActivity();
        if (mainActivity != null) {
            //FragmentTransition.to(AccommodationsListFragment.newInstance(getActivity(), AccommodationListType.SEARCH), getActivity(), true, R.id.accommodations_fragment);
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_search_to_accommodationsListFragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}