package com.example.topmovies.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.topmovies.data.Resource;
import com.example.topmovies.data.local.entity.MoviePoster;
import com.example.topmovies.data.repository.MovieRepository;

import java.util.List;

public class CarouselViewModel extends ViewModel {

    private LiveData<Resource<List<MoviePoster>>> posters;
    private MutableLiveData<Boolean> tryGetMovies = new MutableLiveData<>();

    public CarouselViewModel(MovieRepository repository) {
        posters = Transformations.switchMap(tryGetMovies, ignored -> repository.getPosters());
    }

    public LiveData<Resource<List<MoviePoster>>> getPosters() {
        return posters;
    }

    public void startGetPosters() {
        tryGetMovies.setValue(true);
    }
}
