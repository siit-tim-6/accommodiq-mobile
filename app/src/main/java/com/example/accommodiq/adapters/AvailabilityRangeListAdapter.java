package com.example.accommodiq.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accommodiq.R;
import com.example.accommodiq.models.Availability;

import java.util.List;

public class AvailabilityRangeListAdapter extends RecyclerView.Adapter<AvailabilityRangeListAdapter.ViewHolder> {
    private List<Availability> availabilityRangeList;

    public AvailabilityRangeListAdapter(List<Availability> availabilityRangeList) {
        this.availabilityRangeList = availabilityRangeList;
    }

    @NonNull
    @Override
    public AvailabilityRangeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_price_range, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailabilityRangeListAdapter.ViewHolder holder, int position) {
        Availability availability = availabilityRangeList.get(position);
        holder.textViewFromDate.setText(Math.toIntExact(availability.getFromDate()));
        holder.textViewToDate.setText(Math.toIntExact(availability.getToDate()));
        holder.textViewPrice.setText(String.valueOf(availability.getPrice()));
        holder.buttonDelete.setOnClickListener(view -> {
            availabilityRangeList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return availabilityRangeList != null ? availabilityRangeList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFromDate;
        TextView textViewToDate;
        TextView textViewPrice;
        ImageButton buttonDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewFromDate = itemView.findViewById(R.id.textViewStartDate);
            textViewToDate = itemView.findViewById(R.id.textViewEndDate);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
