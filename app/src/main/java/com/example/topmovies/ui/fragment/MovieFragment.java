package com.example.topmovies.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.topmovies.BuildConfig;
import com.example.topmovies.R;
import com.example.topmovies.data.local.entity.MoviePoster;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieFragment extends Fragment {

    private static final String ARG_MOVIE_POSTER = "moviePoster";
    public  String TAG = this.getClass().getName();

    @BindView(R.id.iv_movie) ImageView movieImage;
    private Unbinder unbinder;

    public static MovieFragment newInstance(MoviePoster moviePoster) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE_POSTER, moviePoster);
        MovieFragment fragment = new MovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_item, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if(args!=null) {
            MoviePoster poster = args.getParcelable(ARG_MOVIE_POSTER);
            if(poster!=null) {
                Glide.with(view)
                        .load(BuildConfig.IMAGE_URL + poster.getPosterImage())
                        .into(movieImage);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
