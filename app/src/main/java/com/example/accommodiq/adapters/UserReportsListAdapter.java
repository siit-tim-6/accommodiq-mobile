package com.example.accommodiq.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.accommodiq.R;
import com.example.accommodiq.databinding.UserReportCardBinding;
import com.example.accommodiq.dtos.UserReportDto;
import com.example.accommodiq.ui.userReport.UserReportBaseObservable;

import java.util.ArrayList;

public class UserReportsListAdapter extends ArrayAdapter<UserReportDto> {
    private final Context context;
    private final ArrayList<UserReportDto> reports;

    public UserReportsListAdapter(Context context, ArrayList<UserReportDto> reports) {
        super(context, R.layout.fragment_user_report_list, reports);
        this.context = context;
        this.reports = reports;
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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserReportDto report = reports.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_report_card, parent, false);
        }
        UserReportCardBinding binding = UserReportCardBinding.bind(convertView);
        binding.setObservable(new UserReportBaseObservable(report));
        return binding.getRoot();
    }
}
