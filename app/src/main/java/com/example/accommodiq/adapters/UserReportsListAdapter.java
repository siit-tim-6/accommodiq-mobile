package com.example.accommodiq.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.ReportsClient;
import com.example.accommodiq.databinding.UserReportCardBinding;
import com.example.accommodiq.dtos.AccountStatusDto;
import com.example.accommodiq.dtos.UserReportDto;
import com.example.accommodiq.ui.userReport.UserReportBaseObservable;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserReportsListAdapter extends ArrayAdapter<UserReportDto> {
    private final Context context;
    private final ArrayList<UserReportDto> reports;

    public UserReportsListAdapter(Context context, ArrayList<UserReportDto> reports) {
        super(context, R.layout.fragment_user_report_list, reports);
        this.context = context;
        this.reports = reports;
        refreshReports();
    }

    @Override
    public int getCount() {
        return reports.size();
    }

    @Nullable
    @Override
    public UserReportDto getItem(int position) {
        return reports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reports.get(position).hashCode();
    }

    @Override
    public void remove(@Nullable UserReportDto object) {
        reports.remove(object);
        notifyDataSetChanged();
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReportsClient reportsClient = RetrofitClientInstance.getRetrofitInstance(getContext()).create(ReportsClient.class);
        UserReportDto report = reports.get(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.user_report_card, parent, false);

        UserReportCardBinding binding = UserReportCardBinding.bind(convertView);
        binding.setObservable(new UserReportBaseObservable(report));
        binding.blockButton.setOnClickListener(v -> {
            Call<Void> call = reportsClient.blockUser(report.getReportedUser().getId(), new AccountStatusDto("BLOCKED"));
            call.enqueue(new retrofit2.Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull retrofit2.Response<Void> response) {
//                    remove(report);
                    refreshReports();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Error while blocking user", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.dismissButton.setOnClickListener(v -> {
            Call<Void> call = reportsClient.deleteReport(report.getId());
            call.enqueue(new retrofit2.Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                    remove(report);
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Error while dismissing report", Toast.LENGTH_SHORT).show();
                }
            });
        });
        return binding.getRoot();
    }

    private void refreshReports() {
            ReportsClient reportsClient = RetrofitClientInstance.getRetrofitInstance(getContext()).create(ReportsClient.class);

            Call<ArrayList<UserReportDto>> call = reportsClient.getReports();
            call.enqueue(new Callback<ArrayList<UserReportDto>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<UserReportDto>> call, @NonNull Response<ArrayList<UserReportDto>> response) {
                    if (response.isSuccessful()) {
                        reports.clear();
                        assert response.body() != null;
                        reports.addAll(response.body());
                        notifyDataSetChanged();
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
