package com.example.topmovies.data.local.entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey
    private int id;
    private String backdropImage;
    private String originalTitle;
    private int duration;
    private String overview;
    private int year;
    private String genres;
    @Nullable private String collectionPoster;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackdropImage() {
        return backdropImage;
    }

    public void setBackdropImage(String backdropImage) {
        this.backdropImage = backdropImage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    @Nullable
    public String getCollectionPoster() {
        return collectionPoster;
    }

    public void setCollectionPoster(@Nullable String collectionPoster) {
        this.collectionPoster = collectionPoster;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", backdropImage='" + backdropImage + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", duration=" + duration +
                ", overview='" + overview + '\'' +
                ", year=" + year +
                ", genres='" + genres + '\'' +
                ", collectionPoster='" + collectionPoster + '\'' +
                '}';
    }
}
