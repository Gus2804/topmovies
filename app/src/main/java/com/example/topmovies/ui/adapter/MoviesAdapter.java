package com.example.topmovies.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.topmovies.data.local.entity.MoviePoster;
import com.example.topmovies.ui.fragment.MovieFragment;

import java.util.List;

public class MoviesAdapter extends FragmentStateAdapter {

    private List<MoviePoster> posters;

    public MoviesAdapter(@NonNull FragmentActivity fragmentActivity, List<MoviePoster> posters) {
        super(fragmentActivity);
        this.posters = posters;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return MovieFragment.newInstance(posters.get(position));
    }

    @Override
    public int getItemCount() {
        return posters.size();
    }
}
