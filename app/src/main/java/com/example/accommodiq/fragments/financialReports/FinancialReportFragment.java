package com.example.accommodiq.fragments.financialReports;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accommodiq.R;
import com.example.accommodiq.adapters.FinancialReportAdapter;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.FinancialReportClient;
import com.example.accommodiq.dtos.FinancialReportEntryDto;
import com.example.accommodiq.utils.DateUtils;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinancialReportFragment extends ListFragment {
    private List<FinancialReportEntryDto> entries;
    private FinancialReportClient financialReportClient;
    private Long dateFrom;
    private Long dateTo;
    private ArrayList<String> colors = new ArrayList<>();

    public FinancialReportFragment() {
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
        return inflater.inflate(R.layout.fragment_financial_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnSearch = view.findViewById(R.id.financial_report_search_btn);
        Button exportToPdfBtn = view.findViewById(R.id.export_to_pdf_btn);
        Button accommodationReportsBtn = view.findViewById(R.id.accommodation_reports_btn);
        TextView dateRangeSearch = view.findViewById(R.id.financial_report_date_range_search);
        TextView totalRevenue = view.findViewById(R.id.financial_report_total_revenue);
        PieChart chart = view.findViewById(R.id.reports_pie_chart);
        CalendarConstraints dateValidator = (new CalendarConstraints.Builder()).setValidator(DateValidatorPointBackward.now()).build();
        MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder.dateRangePicker().setCalendarConstraints(dateValidator).setTheme(R.style.ThemeMaterialCalendar).setTitleText("Select check-in and check-out date").setSelection(new Pair<>(null, null)).build();

        dateRangeSearch.setOnClickListener(v -> {
            if (!dateRangePicker.isAdded())
                dateRangePicker.show(this.getParentFragmentManager(), "AccommodIQ");

            dateRangePicker.addOnNegativeButtonClickListener(v1 -> {
                dateRangePicker.dismiss();
            });

            dateRangePicker.addOnPositiveButtonClickListener(selection -> {
                dateFrom = selection.first;
                dateTo = selection.second;
                dateRangeSearch.setText(String.format("%s - %s", DateUtils.convertTimeToDate(selection.first), DateUtils.convertTimeToDate(selection.second)));
            });
        });

        btnSearch.setOnClickListener(v -> {
            searchEntries(chart, totalRevenue);
        });

        accommodationReportsBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_financialReportsFragment_to_financialReportsMonthlyFragment);
        });
    }

    private void searchEntries(PieChart chart, TextView totalRevenue) {
        Call<List<FinancialReportEntryDto>> entriesCall = financialReportClient.getFinancialReport(dateFrom, dateTo);

        entriesCall.enqueue(new Callback<List<FinancialReportEntryDto>>() {
            @Override
            public void onResponse(Call<List<FinancialReportEntryDto>> call, Response<List<FinancialReportEntryDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    entries = response.body();
                    for (FinancialReportEntryDto entry : entries) {
                        String color = getRandomHexColor();
                        chart.addPieSlice(new PieModel(
                                entry.getAccommodationTitle(),
                                ((int) entry.getRevenue()),
                                Color.parseColor(color)
                        ));
                        colors.add(color);
                        totalRevenue.setText(String.format("Total Revenue %d€", entries.stream().mapToInt(entryDto -> ((int) entryDto.getRevenue())).sum()));
                    }
                    FinancialReportAdapter adapter = new FinancialReportAdapter(getActivity(), (ArrayList<FinancialReportEntryDto>) entries, colors);
                    setListAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error happened", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FinancialReportEntryDto>> call, Throwable t) {
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