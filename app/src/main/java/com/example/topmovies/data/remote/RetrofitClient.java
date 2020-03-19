package com.example.topmovies.data.remote;

import android.util.Log;

import com.example.topmovies.BuildConfig;
import com.example.topmovies.data.remote.api.MoviesApi;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient client;
    private Retrofit retrofit;

    public static RetrofitClient getInstance() {
        if(client==null) {
            client = new RetrofitClient();
        }
        return client;
    }

    public RetrofitClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? Level.BODY : Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    HttpUrl url = chain.request().url().newBuilder()
                            .setQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                            .setQueryParameter("language","es-MX")
                            .build();
                    Request request = chain.request().newBuilder().url(url).build();
                    return chain.proceed(request);
                })
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public MoviesApi getMoviesApi() {
        return retrofit.create(MoviesApi.class);
    }

}
