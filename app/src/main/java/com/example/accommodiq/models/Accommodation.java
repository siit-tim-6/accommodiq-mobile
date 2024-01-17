package com.example.accommodiq.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.accommodiq.R;
import com.example.accommodiq.dtos.AccommodationReviewDto;

public class Accommodation implements Parcelable {
    private long id;
    private String title;
    private String description;
    private double rating;
    private int reviewCount;
    private String location;
    private int minGuests;
    private int maxGuests;
    // for demonstration purposes only, will be an URL later
    private int image;
    private double price;
    private String status;

    public Accommodation(long id, String title, String description, double rating, int reviewCount, String location, int minGuests, int maxGuests, int image, double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.location = location;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.image = image;
        this.price = price;
    }

    public Accommodation() {

    }

    protected Accommodation(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        rating = in.readDouble();
        reviewCount = in.readInt();
        location = in.readString();
        minGuests = in.readInt();
        maxGuests = in.readInt();
        image = in.readInt();
        price = in.readDouble();
    }

    public Accommodation(AccommodationReviewDto accommodationReviewDto) {
        this.id = accommodationReviewDto.getId();
        this.title = accommodationReviewDto.getTitle();
        this.description = accommodationReviewDto.getDescription();
        this.rating = accommodationReviewDto.getRating();
        this.reviewCount = accommodationReviewDto.getReviewCount();
        this.location = accommodationReviewDto.getLocation();
        this.minGuests = accommodationReviewDto.getMinGuests();
        this.maxGuests = accommodationReviewDto.getMaxGuests();
        this.image = R.drawable.accommodation_image;
        this.price = accommodationReviewDto.getMinPrice();
        this.status = accommodationReviewDto.getStatus();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeDouble(rating);
        parcel.writeInt(reviewCount);
        parcel.writeString(location);
        parcel.writeInt(minGuests);
        parcel.writeInt(maxGuests);
        parcel.writeInt(image);
        parcel.writeDouble(price);
    }

    public static final Creator<Accommodation> CREATOR = new Creator<Accommodation>() {
        @Override
        public Accommodation createFromParcel(Parcel parcel) {
            return new Accommodation(parcel);
        }

        @Override
        public Accommodation[] newArray(int i) {
            return new Accommodation[i];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
