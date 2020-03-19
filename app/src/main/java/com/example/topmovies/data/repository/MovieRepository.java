package com.example.topmovies.data.repository;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.topmovies.data.Resource;
import com.example.topmovies.data.local.MovieDao;
import com.example.topmovies.data.local.entity.Movie;
import com.example.topmovies.data.local.entity.MoviePoster;
import com.example.topmovies.data.remote.api.MoviesApi;
import com.example.topmovies.data.remote.model.GenreBean;
import com.example.topmovies.data.remote.model.MovieBean;
import com.example.topmovies.data.remote.model.MovieDetailBean;
import com.example.topmovies.data.remote.model.TopMoviesBean;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class MovieRepository {

    private MoviesApi api;
    private MovieDao dao;

    public MovieRepository(MoviesApi api, MovieDao dao) {
        this.api = api;
        this.dao = dao;
    }

    public LiveData<Resource<List<MoviePoster>>> getPosters() {
        MediatorLiveData<Resource<List<MoviePoster>>> postersLiveData = new MediatorLiveData<>();
        LiveData<List<MoviePoster>> query = dao.getPosters();
        postersLiveData.addSource(query, posters -> {
            //The db contains posters
            if(!posters.isEmpty()) {
                postersLiveData.setValue(Resource.success(posters));
            } else {
                postersLiveData.removeSource(query);
                Map<String, String> params = new HashMap<>();
                params.put("region", "mx");
                params.put("page", "1");
                Call<TopMoviesBean> call = api.getTopMovies(params);
                postersLiveData.setValue(Resource.loading());
                call.enqueue(new Callback<TopMoviesBean>() {
                    @Override
                    public void onResponse(Call<TopMoviesBean> call, Response<TopMoviesBean> response) {
                        TopMoviesBean bean = response.body();
                        if(bean!=null) {
                            List<MovieBean> firstTen = bean.getMovies().size() > 10 ? bean.getMovies().subList(0, 10) : bean.getMovies();
                            List<MoviePoster> moviePosters = new ArrayList<>();
                            Log.d("Repository", firstTen.toString());
                            for (MovieBean movieBean : firstTen) {
                                MoviePoster poster = new MoviePoster(
                                        movieBean.getId(),
                                        movieBean.getTitle(),
                                        movieBean.getPosterPath(),
                                        firstTen.indexOf(movieBean)
                                );
                                moviePosters.add(poster);
                            }
                            AsyncTask.execute(() -> {
                                for (MoviePoster poster :
                                        moviePosters) {
                                    dao.save(poster);
                                }
                                postersLiveData.postValue(Resource.success(moviePosters));
                            });
                        } else {
                            postersLiveData.setValue(Resource.success(new ArrayList<>()));
                        }
                    }

                    @Override
                    public void onFailure(Call<TopMoviesBean> call, Throwable t) {
                        if(t instanceof SocketTimeoutException) {
                            postersLiveData.setValue(Resource.error("Ha ocurrido un error de conexión"));
                        }
                        if(t instanceof HttpException) {
                            postersLiveData.setValue(Resource.error("Ha ocurrido un error en la petición"));
                        }
                        postersLiveData.setValue(Resource.error("Ha ocurrido un error"));
                    }
                });
            }
        });
        return postersLiveData;
    }

    public LiveData<Resource<Movie>> getMovieDetails(int id) {
        MediatorLiveData<Resource<Movie>> movieLiveData = new MediatorLiveData<>();
        LiveData<List<Movie>> query = dao.getMovie(id);
        movieLiveData.addSource(query, movies -> {
            //The db contains movies
            if(!movies.isEmpty()) {
                movieLiveData.setValue(Resource.success(movies.get(0)));
            } else {
                movieLiveData.removeSource(query);
                Call<MovieDetailBean> call = api.getMovieDetail(id);
                movieLiveData.setValue(Resource.loading());
                call.enqueue(new Callback<MovieDetailBean>() {
                    @Override
                    public void onResponse(Call<MovieDetailBean> call, Response<MovieDetailBean> response) {
                        MovieDetailBean bean = response.body();
                        if(bean!=null) {
                            Movie movie = new Movie();
                            movie.setBackdropImage(bean.getBackdropPath());
                            movie.setOriginalTitle(bean.getOriginalTitle());
                            movie.setDuration(bean.getRuntime());
                            movie.setOverview(bean.getOverview());
                            if(bean.getReleaseDate()!=null) {
                                try {
                                    movie.setYear(Integer.parseInt(bean.getReleaseDate().substring(0, 4)));
                                } catch (NumberFormatException ex) {
                                    movie.setYear(0);
                                }
                            }
                            else {
                                movie.setYear(0);
                            }
                            String genres = "";
                            List<GenreBean> genreBeanList = bean.getGenres();
                            for (GenreBean genre : genreBeanList) {
                                genres = genres
                                        .concat(genre.getName())
                                        .concat(genreBeanList.indexOf(genre) + 1 == genreBeanList.size() ? "" : ", ");
                            }
                            movie.setGenres(genres);
                            if(bean.getBelongsToCollection()!=null) {
                                movie.setCollectionPoster(bean.getBelongsToCollection().getPosterPath());
                            }
                            AsyncTask.execute(() -> {
                                dao.save(movie);
                                movieLiveData.postValue(Resource.success(movie));
                            });
                        } else {
                            movieLiveData.setValue(Resource.error("No se encontró el elemento"));
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDetailBean> call, Throwable t) {
                        if(t instanceof SocketTimeoutException) {
                            movieLiveData.setValue(Resource.error("Ha ocurrido un error de conexión"));
                        }
                        if(t instanceof HttpException) {
                            movieLiveData.setValue(Resource.error("Ha ocurrido un error en la petición"));
                        }
                        movieLiveData.setValue(Resource.error("Ha ocurrido un error"));
                    }
                });
            }
        });
        return movieLiveData;
    }

}
