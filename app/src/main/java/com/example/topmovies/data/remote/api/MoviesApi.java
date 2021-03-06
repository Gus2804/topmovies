package com.example.topmovies.data.remote.api;

import com.example.topmovies.data.remote.model.MovieDetailBean;
import com.example.topmovies.data.remote.model.TopMoviesBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface MoviesApi {

    @GET("movie/top_rated")
    Call<TopMoviesBean> getTopMovies(@QueryMap Map<String, String> params);

    @GET("movie/{id}")
    Call<MovieDetailBean> getMovieDetail(@Path("id") int id);

}
