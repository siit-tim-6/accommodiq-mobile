package com.example.accommodiq.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.accommodiq.R;
import com.example.accommodiq.dtos.FinancialReportMonthlyEntryDto;

import java.util.ArrayList;

public class FinancialReportMonthlyAdapter extends ArrayAdapter<FinancialReportMonthlyEntryDto> {
    private final ArrayList<FinancialReportMonthlyEntryDto> entries;
    private final ArrayList<String> colors;

    public FinancialReportMonthlyAdapter(Context context, ArrayList<FinancialReportMonthlyEntryDto> entries, ArrayList<String> colors) {
        super(context, R.layout.financial_report_card, entries);
        this.entries = entries;
        this.colors = colors;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public FinancialReportMonthlyEntryDto getItem(int position) {
        return entries.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.financial_report_card, parent, false);
        }

        FinancialReportMonthlyEntryDto entry = getItem(position);

        TextView title = convertView.findViewById(R.id.financial_report_title);
        TextView profit = convertView.findViewById(R.id.financial_report_profit);
        TextView reservationCount = convertView.findViewById(R.id.financial_report_reservations);
        Button colorBtn = convertView.findViewById(R.id.financial_report_color);

        if (entry != null) {
            String profitString = entry.getRevenue() + "€";
            String reservationCountString = entry.getReservationCount() + " reservations";

            title.setText(entry.getMonth());
            profit.setText(profitString);
            reservationCount.setText(reservationCountString);
            colorBtn.setBackgroundColor(Color.parseColor(colors.get(position)));
        }

        return convertView;
    }
}
