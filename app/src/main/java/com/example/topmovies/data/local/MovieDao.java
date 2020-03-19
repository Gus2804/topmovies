package com.example.topmovies.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.topmovies.data.local.entity.MoviePoster;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDao {

    @Insert(onConflict = REPLACE)
    void save(MoviePoster moviePoster);

    @Query("SELECT * FROM movie_posters")
    LiveData<List<MoviePoster>> getPosters();

}
