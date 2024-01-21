package com.example.accommodiq.fragments.financialReports;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.FinancialReportMonthlyAdapter;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.FinancialReportClient;
import com.example.accommodiq.dtos.AccommodationTitleDto;
import com.example.accommodiq.dtos.FinancialReportEntryDto;
import com.example.accommodiq.dtos.FinancialReportMonthlyEntryDto;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinancialReportMonthlyFragment extends ListFragment {

    private List<FinancialReportMonthlyEntryDto> entries;
    private List<AccommodationTitleDto> titles;
    private FinancialReportClient financialReportClient;
    private ArrayList<String> colors = new ArrayList<>();

    public FinancialReportMonthlyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        financialReportClient = RetrofitClientInstance.getRetrofitInstance(getContext()).create(FinancialReportClient.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_financial_report_monthly, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnSearch = view.findViewById(R.id.financial_report_search_monthly_btn);
        Button exportToPdfBtn = view.findViewById(R.id.export_to_pdf_monthly_btn);
        Spinner accommodationSelect = view.findViewById(R.id.financial_report_monthly_accommodation);
        EditText yearInput = view.findViewById(R.id.financial_report_monthly_year);
        TextView totalRevenue = view.findViewById(R.id.financial_report_total_revenue_monthly);
        PieChart chart = view.findViewById(R.id.monthly_reports_pie_chart);

        Call<List<AccommodationTitleDto>> titlesCall = financialReportClient.getAccommodationTitles();

        titlesCall.enqueue(new Callback<List<AccommodationTitleDto>>() {
            @Override
            public void onResponse(Call<List<AccommodationTitleDto>> call, Response<List<AccommodationTitleDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    titles = response.body();
                    accommodationSelect.setAdapter(new ArrayAdapter<>(
                            getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            titles));
                } else {
                    Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationTitleDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
            }
        });

        btnSearch.setOnClickListener(v -> {
            int selectedYear;

            try {
                selectedYear = Integer.parseInt(yearInput.getText().toString());
            } catch (NumberFormatException ex) {
                return;
            }

            if (accommodationSelect.getSelectedItem() == null) {
                return;
            }

            Long selectedAccommodationId = (((AccommodationTitleDto) accommodationSelect.getSelectedItem()).getId());
            searchEntries(selectedAccommodationId, selectedYear, chart, totalRevenue);
        });
    }

    private void searchEntries(Long accommodationId, int year, PieChart chart, TextView totalRevenue) {
        Call<List<FinancialReportMonthlyEntryDto>> entriesCall = financialReportClient.getAccommodationFinancialReport(accommodationId, year);

        entriesCall.enqueue(new Callback<List<FinancialReportMonthlyEntryDto>>() {
            @Override
            public void onResponse(Call<List<FinancialReportMonthlyEntryDto>> call, Response<List<FinancialReportMonthlyEntryDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    entries = response.body();
                    for (FinancialReportMonthlyEntryDto entry : entries) {
                        String color = getRandomHexColor();
                        chart.addPieSlice(new PieModel(
                                entry.getMonth(),
                                ((int) entry.getRevenue()),
                                Color.parseColor(color)
                        ));
                        colors.add(color);
                        totalRevenue.setText(String.format("Total Revenue %d€", entries.stream().mapToInt(entryDto -> ((int) entryDto.getRevenue())).sum()));
                    }
                    FinancialReportMonthlyAdapter adapter = new FinancialReportMonthlyAdapter(getContext(), (ArrayList<FinancialReportMonthlyEntryDto>) entries, colors);
                    setListAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FinancialReportMonthlyEntryDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRandomHexColor() {
        String[] possibleChars = {"A", "B", "C", "D", "E", "F", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        StringBuilder stringBuilder = new StringBuilder("#");
        for (int i = 0; i < 6; ++i) {
            stringBuilder.append(possibleChars[(int) (Math.random() * possibleChars.length)]);
        }

        return stringBuilder.toString();
    }
}