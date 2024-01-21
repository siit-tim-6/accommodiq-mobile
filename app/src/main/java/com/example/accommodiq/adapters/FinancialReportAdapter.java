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
import com.example.accommodiq.dtos.FinancialReportEntryDto;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FinancialReportAdapter extends ArrayAdapter<FinancialReportEntryDto> {
    private final ArrayList<FinancialReportEntryDto> entries;
    private final Context context;
    private final ArrayList<String> colors;

    public FinancialReportAdapter(Context context, ArrayList<FinancialReportEntryDto> entries, ArrayList<String> colors) {
        super(context, R.layout.financial_report_card, entries);
        this.context = context;
        this.entries = entries;
        this.colors = colors;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Nullable
    @Override
    public FinancialReportEntryDto getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.financial_report_card, parent, false);
        }

        FinancialReportEntryDto entry = getItem(position);

        TextView title = convertView.findViewById(R.id.financial_report_title);
        TextView profit = convertView.findViewById(R.id.financial_report_profit);
        TextView reservationCount = convertView.findViewById(R.id.financial_report_reservations);
        Button colorBtn = convertView.findViewById(R.id.financial_report_color);

        if (entry != null) {
            String profitString = entry.getRevenue() + "€";
            String reservationCountString = entry.getReservationCount() + " reservations";

            title.setText(entry.getAccommodationTitle());
            profit.setText(profitString);
            reservationCount.setText(reservationCountString);
            colorBtn.setBackgroundColor(Color.parseColor(colors.get(position)));
        }

        return convertView;
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
