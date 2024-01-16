package com.example.accommodiq.ui.userReport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.UserReportsListAdapter;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.ReportsClient;
import com.example.accommodiq.dtos.UserReportDto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserReportListFragment extends ListFragment {
    private UserReportsListAdapter adapter;
    private List<UserReportDto> reports;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getReports();
        return inflater.inflate(R.layout.fragment_user_report_list, container, false);
    }

    private void getReports() {
        ReportsClient reportsClient = RetrofitClientInstance.getRetrofitInstance(getContext()).create(ReportsClient.class);

        Call<ArrayList<UserReportDto>> call = reportsClient.getReports();
        call.enqueue(new Callback<ArrayList<UserReportDto>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<UserReportDto>> call, @NonNull Response<ArrayList<UserReportDto>> response) {
                if (response.isSuccessful()) {
                    reports = response.body();
                    adapter = new UserReportsListAdapter(getContext(), (ArrayList<UserReportDto>) reports);
                    setListAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<UserReportDto>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
