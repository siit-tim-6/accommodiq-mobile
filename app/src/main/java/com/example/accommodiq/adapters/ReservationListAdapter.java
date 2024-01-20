package com.example.accommodiq.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.ReservationClient;
import com.example.accommodiq.dtos.ReservationCardDto;
import com.example.accommodiq.enums.ReservationListType;
import com.example.accommodiq.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReservationListAdapter extends ArrayAdapter<ReservationCardDto> {
    private final ArrayList<ReservationCardDto> reservations;
    private final Context context;
    private final ReservationListType type;
    private int layoutViewId;
    private ArrayList<Long> cancellableReservationIds;

    public ReservationListAdapter(Context context, ArrayList<ReservationCardDto> reservations, ReservationListType type, ArrayList<Long> cancellableReservationIds) {
        super(context, R.layout.reservation_card, reservations);
        this.reservations = reservations;
        this.context = context;
        this.type = type;
        this.cancellableReservationIds = cancellableReservationIds;
    }

    public void setCancellableReservationIds(ArrayList<Long> cancellableReservationIds) {
        this.cancellableReservationIds = cancellableReservationIds;
    }

    @Override
    public int getCount() {
        return reservations.size();
    }

    @Nullable
    @Override
    public ReservationCardDto getItem(int position) {
        return reservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reservations.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReservationClient reservationClient = RetrofitClientInstance.getRetrofitInstance(context).create(ReservationClient.class);
        ReservationCardDto reservation = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservation_card, parent, false);
        }

        LinearLayout reservationCard = convertView.findViewById(R.id.reservation_card_item);
        ImageView imageView = convertView.findViewById(R.id.reservation_accommodation_image);
        TextView titleTextView = convertView.findViewById(R.id.reservation_accommodation_title);
        TextView locationTextView = convertView.findViewById(R.id.reservation_accommodation_location);
        TextView guestsTextView = convertView.findViewById(R.id.reservation_guests);
        TextView dateRangeTextView = convertView.findViewById(R.id.reservation_date_range);
        TextView statusTextView = convertView.findViewById(R.id.reservation_status);
        TextView totalPriceTextView = convertView.findViewById(R.id.reservation_total_price);
        Button deleteReservationButton = convertView.findViewById(R.id.delete_reservation);
        Button cancelReservationButton = convertView.findViewById(R.id.cancel_reservation);

        if (reservation != null) {
            String guests = reservation.getGuests() + " guests";
            String dateRange = DateUtils.convertTimeToDate(reservation.getStartDate()) + " - " + DateUtils.convertTimeToDate(reservation.getEndDate());
            String totalPrice = reservation.getTotalPrice() + " € total";

            if (!reservation.getAccommodationImage().isEmpty()) {
                String imageUrl = RetrofitClientInstance.getServerIp(getContext()) + "/images/" + reservation.getAccommodationImage();
                Picasso.get().load(imageUrl).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.accommodation_image);
            }
            titleTextView.setText(reservation.getAccommodationTitle());
            locationTextView.setText(reservation.getAccommodationLocation().getAddress());
            guestsTextView.setText(guests);
            dateRangeTextView.setText(dateRange);
            statusTextView.setText(reservation.getStatus());
            totalPriceTextView.setText(totalPrice);

            deleteReservationButton.setOnClickListener(v -> {
                Toast.makeText(context, "Reservation deleted", Toast.LENGTH_SHORT).show();
            });

            cancelReservationButton.setOnClickListener(v -> {
                Toast.makeText(context, "Reservation cancelled", Toast.LENGTH_SHORT).show();
            });

            if (Objects.equals(reservation.getStatus(), "PENDING")) {
                Log.i("ADAPTER", "getView: ID: " + reservation.getId() + " DELETABLE");
                cancelReservationButton.setVisibility(View.GONE);
                deleteReservationButton.setVisibility(View.VISIBLE);
                deleteReservationButton.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                deleteReservationButton.requestLayout();
            } else if (cancellableReservationIds.contains(reservation.getId())) {
                Log.i("ADAPTER", "getView: ID: " + reservation.getId() + " CANCELABLE");
                deleteReservationButton.setVisibility(View.GONE);
                cancelReservationButton.setVisibility(View.VISIBLE);
                cancelReservationButton.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                cancelReservationButton.requestLayout();
            } else {
                Log.i("ADAPTER", "getView: ID: " + reservation.getId() + " NONE");
                cancelReservationButton.setVisibility(View.GONE);
                deleteReservationButton.setVisibility(View.GONE);
            }
            notifyDataSetChanged();

            reservationCard.setOnClickListener(v -> {
                Toast.makeText(context, "Clicked accommodation", Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }
}
