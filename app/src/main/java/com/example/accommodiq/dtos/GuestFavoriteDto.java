package com.example.accommodiq.dtos;

public class GuestFavoriteDto {
    private long favoriteId;

    public GuestFavoriteDto() {
    }

    public GuestFavoriteDto(long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(long favoriteId) {
        this.favoriteId = favoriteId;
    }
}
