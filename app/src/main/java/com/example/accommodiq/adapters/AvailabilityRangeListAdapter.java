package com.example.accommodiq.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accommodiq.R;
import com.example.accommodiq.dtos.AvailabilityDto;
import com.example.accommodiq.listener.OnAvailabilityRemovedListener;
import com.example.accommodiq.models.Availability;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AvailabilityRangeListAdapter extends RecyclerView.Adapter<AvailabilityRangeListAdapter.ViewHolder> {
    private List<Availability> availabilityRangeList;
    private OnAvailabilityRemovedListener listener;

    public AvailabilityRangeListAdapter(List<Availability> availabilityRangeList, OnAvailabilityRemovedListener listener) {
        this.availabilityRangeList = availabilityRangeList;
        this.listener = listener;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.textViewFromDate.setText(dateFormat.format(new Date(availability.getFromDate())));
        holder.textViewToDate.setText(dateFormat.format(new Date(availability.getToDate())));
        holder.textViewPrice.setText(String.valueOf(availability.getPrice()));
        holder.buttonDelete.setOnClickListener(view -> {
            int positionToRemove = holder.getAdapterPosition();
            if (positionToRemove != RecyclerView.NO_POSITION) {
                removeItem(positionToRemove);
            }
        });
    }

    @Override
    public int getItemCount() {
        return availabilityRangeList != null ? availabilityRangeList.size() : 0;
    }

    public void removeItem(int position) {
        if (position < 0 || position >= availabilityRangeList.size()) {
            return; // Out of bounds
        }
        Availability removedItem = availabilityRangeList.get(position);
        availabilityRangeList.remove(position);
        notifyItemRemoved(position);
        if (listener != null) {
            listener.onAvailabilityRemoved(removedItem);
        }
    }

    public void addItem(Availability availability) {
        availabilityRangeList.add(availability);
        notifyItemInserted(availabilityRangeList.size() - 1);
    }

    public void setAvailabilityRangeList(List<Availability> availabilityRangeList) {
        this.availabilityRangeList=availabilityRangeList;
        notifyDataSetChanged();
    }

    public List<Availability> getAvailabilityRangeList() { return availabilityRangeList; }

    public List<AvailabilityDto> getAvailabilityDtoList() {
        return availabilityRangeList.stream()
                .map(AvailabilityDto::new)
                .collect(Collectors.toList());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFromDate;
        TextView textViewToDate;
        TextView textViewPrice;
        Button buttonDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewFromDate = itemView.findViewById(R.id.textViewFromDate);
            textViewToDate = itemView.findViewById(R.id.textViewToDate);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
