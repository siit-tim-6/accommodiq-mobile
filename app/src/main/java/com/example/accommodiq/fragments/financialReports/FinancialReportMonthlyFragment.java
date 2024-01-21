package com.example.accommodiq.fragments.financialReports;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.FinancialReportClient;
import com.example.accommodiq.dtos.FinancialReportEntryDto;
import com.example.accommodiq.dtos.FinancialReportMonthlyEntryDto;

import java.util.ArrayList;
import java.util.List;

public class FinancialReportMonthlyFragment extends ListFragment {

    private List<FinancialReportMonthlyEntryDto> entries;
    private FinancialReportClient financialReportClient;
    private Long dateFrom;
    private Long dateTo;
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
}