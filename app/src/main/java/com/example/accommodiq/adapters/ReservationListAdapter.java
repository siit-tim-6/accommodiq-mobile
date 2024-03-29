package com.example.accommodiq.adapters;

import android.content.Context;
import android.os.Bundle;
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
import androidx.navigation.Navigation;

import com.example.accommodiq.R;
import com.example.accommodiq.apiConfig.RetrofitClientInstance;
import com.example.accommodiq.clients.ReservationClient;
import com.example.accommodiq.dtos.MessageDto;
import com.example.accommodiq.dtos.ReservationCardDto;
import com.example.accommodiq.dtos.ReservationStatusDto;
import com.example.accommodiq.enums.ReservationListType;
import com.example.accommodiq.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationListAdapter extends ArrayAdapter<ReservationCardDto> {
    private final ArrayList<ReservationCardDto> reservations;
    private final Context context;
    private final ReservationListType type;
    private final ArrayList<Long> cancellableReservationIds;

    public ReservationListAdapter(Context context, ArrayList<ReservationCardDto> reservations, ReservationListType type, ArrayList<Long> cancellableReservationIds) {
        super(context, R.layout.reservation_card, reservations);
        this.reservations = reservations;
        this.context = context;
        this.type = type;
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
        Button acceptReservationButton = convertView.findViewById(R.id.accept_reservation);
        Button declineReservationButton = convertView.findViewById(R.id.decline_reservation);
        LinearLayout guestControls = convertView.findViewById(R.id.guest_reservation_controls);
        LinearLayout hostControls = convertView.findViewById(R.id.host_reservation_controls);
        TextView guestInfoTextView = convertView.findViewById(R.id.reservation_guest_info);

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
                Call<MessageDto> deleteReservationCall = reservationClient.delete(reservation.getId());

                deleteReservationCall.enqueue(new Callback<MessageDto>() {
                    @Override
                    public void onResponse(@NonNull Call<MessageDto> call, @NonNull Response<MessageDto> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(context, "Reservation deleted successfully!", Toast.LENGTH_SHORT).show();
                            reservations.remove(reservation);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Error while deleting reservation.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MessageDto> call, @NonNull Throwable t) {
                        Toast.makeText(context, "Error while deleting reservation.", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            cancelReservationButton.setOnClickListener(v -> changeReservationStatus("CANCELLED", reservationClient, reservation));

            acceptReservationButton.setOnClickListener(v -> changeReservationStatus("ACCEPTED", reservationClient, reservation));

            declineReservationButton.setOnClickListener(v -> changeReservationStatus("DECLINED", reservationClient, reservation));

            if (type == ReservationListType.GUEST) {
                guestInfoTextView.setVisibility(View.GONE);
                hostControls.setVisibility(View.GONE);
                guestControls.setVisibility(View.VISIBLE);
                if (Objects.equals(reservation.getStatus(), "PENDING")) {
                    Log.i("ADAPTER", "getView: ID: " + reservation.getId() + " DELETABLE");
                    cancelReservationButton.setVisibility(View.GONE);
                    deleteReservationButton.setVisibility(View.VISIBLE);
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
            } else if (type == ReservationListType.HOST) {
                String guestInfo = reservation.getGuestName() + " (" + reservation.getPastCancellations() + " cancellations)";
                guestInfoTextView.setVisibility(View.VISIBLE);
                guestInfoTextView.setText(guestInfo);
                guestControls.setVisibility(View.GONE);
                hostControls.setVisibility(View.VISIBLE);
                if (!Objects.equals(reservation.getStatus(), "PENDING")) {
                    hostControls.setVisibility(View.GONE);
                }
            }
            notifyDataSetChanged();

            reservationCard.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(v);
                Bundle args = new Bundle();
                args.putLong("accommodationId", reservation.getAccommodationId());
                navController.navigate(R.id.action_reservations_to_accommodationDetailsFragment, args);
            });
        }

        return convertView;
    }

    private void changeReservationStatus(String status, ReservationClient reservationClient, ReservationCardDto reservation) {
        Call<ReservationCardDto> changeReservationStatusCall = reservationClient.changeStatus(reservation.getId(), new ReservationStatusDto(status));

        changeReservationStatusCall.enqueue(new Callback<ReservationCardDto>() {
            @Override
            public void onResponse(@NonNull Call<ReservationCardDto> call, @NonNull Response<ReservationCardDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, "Reservation " + status + " successfully!", Toast.LENGTH_SHORT).show();
                    reservations.remove(reservation);
                    String previousGuestName = reservation.getGuestName();
                    int previousCancellations = reservation.getPastCancellations();
                    response.body().setGuestName(previousGuestName);
                    response.body().setPastCancellations(previousCancellations);
                    reservations.add(response.body());
                    if (Objects.equals(status, "CANCELLED")) {
                        cancellableReservationIds.remove(reservation.getId());
                    }
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Error while changing reservation status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReservationCardDto> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error while changing reservation status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sortReservationsByDate() {
        if (reservations.isEmpty()) {
            return;
        }

        boolean isAscending = IntStream.range(1, reservations.size())
                .allMatch(i -> reservations.get(i - 1).getStartDate() <= reservations.get(i).getStartDate());


        reservations.sort(isAscending
                ? Comparator.comparingLong(ReservationCardDto::getStartDate).reversed()
                : Comparator.comparingLong(ReservationCardDto::getStartDate));

        notifyDataSetChanged();
    }

}
