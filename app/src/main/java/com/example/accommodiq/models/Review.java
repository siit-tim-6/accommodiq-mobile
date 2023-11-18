package com.example.accommodiq.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Review implements Parcelable {
    private long id;
    private String userName;
    private double rating;
    private String date;
    private String content;

    public Review(long id, String userName, double rating, String date, String content) {
        this.id = id;
        this.userName = userName;
        this.rating = rating;
        this.date = date;
        this.content = content;
    }

    public Review() {

    }

    protected Review(Parcel in) {
        this.id = in.readLong();
        this.userName = in.readString();
        this.rating = in.readDouble();
        this.date = in.readString();
        this.content = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.userName);
        parcel.writeDouble(this.rating);
        parcel.writeString(this.date);
        parcel.writeString(this.content);
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
