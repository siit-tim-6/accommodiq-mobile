package com.example.accommodiq.ui.report;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.JwtUtils;
import com.example.accommodiq.dtos.ReportRequestDto;
import com.example.accommodiq.ui.account.ProfileViewModel;

public class ReportFragment extends Fragment {

    private ReportViewModel reportViewModel;
    private Long accountId;
    private boolean shouldNavigateBack = false;
    private EditText editTextReason;
    private Button buttonReport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("accountId")) {
            accountId = args.getLong("accountId");
        }else {
            shouldNavigateBack = true;
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (shouldNavigateBack) {
            Toast.makeText(getContext(), "Invalid arguments", Toast.LENGTH_SHORT).show();
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        }

        reportViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ReportViewModel(getContext());
            }
        }).get(ReportViewModel.class);

        editTextReason = view.findViewById(R.id.editTextReason);
        buttonReport = view.findViewById(R.id.button_report);

        buttonReport.setOnClickListener(v -> {
            String reason = editTextReason.getText().toString();
            if(reason.length()<3){
                Toast.makeText(getContext(), "Reason must be at least 3 characters long", Toast.LENGTH_SHORT).show();
                return;
            }
            reportViewModel.reportUser(accountId, new ReportRequestDto(reason));
            editTextReason.setText("");
        });

        reportViewModel.getMessageLiveData().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                if (message.equals("Reported Successfully")
                        || message.equals("Cannot report user without a past reservation.")
                        || message.equals("Cannot report user more than once per reservation.")) {
                    Navigation.findNavController(view).navigateUp();
                }
            }
        });
    }

}