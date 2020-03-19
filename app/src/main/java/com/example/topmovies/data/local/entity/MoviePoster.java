package com.example.topmovies.data.local.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_posters")
public class MoviePoster implements Parcelable {
    @PrimaryKey
    private int id;
    private String title;
    private String posterImage;
    private int order;

    public MoviePoster(int id, String title, String posterImage, int order) {
        this.id = id;
        this.title = title;
        this.posterImage = posterImage;
        this.order = order;
    }

    protected MoviePoster(Parcel in) {
        id = in.readInt();
        title = in.readString();
        posterImage = in.readString();
        order = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(posterImage);
        dest.writeInt(order);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "MoviePoster{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterImage='" + posterImage + '\'' +
                '}';
    }

    public static final Creator<MoviePoster> CREATOR = new Creator<MoviePoster>() {
        @Override
        public MoviePoster createFromParcel(Parcel in) {
            return new MoviePoster(in);
        }

        @Override
        public MoviePoster[] newArray(int size) {
            return new MoviePoster[size];
        }
    };
}
