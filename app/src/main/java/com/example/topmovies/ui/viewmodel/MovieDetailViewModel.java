package com.example.topmovies.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.topmovies.data.Resource;
import com.example.topmovies.data.local.entity.Movie;
import com.example.topmovies.data.repository.MovieRepository;

public class MovieDetailViewModel extends ViewModel {

    private LiveData<Resource<Movie>> movie;
    private MutableLiveData<Integer> idLiveData = new MutableLiveData<>();

    private int id;

    public MovieDetailViewModel(MovieRepository repository) {
        movie = Transformations.switchMap(idLiveData, repository::getMovieDetails);
    }

    public void setMovieId(int id) {
        this.id = id;
    }

    public LiveData<Resource<Movie>> getMovie() {
        return movie;
    }

    public void getMovieDetails() {
        idLiveData.setValue(id);
    }
}
