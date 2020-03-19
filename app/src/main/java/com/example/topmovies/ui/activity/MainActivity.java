package com.example.topmovies.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.topmovies.R;
import com.example.topmovies.data.local.MovieDatabase;
import com.example.topmovies.data.local.entity.MoviePoster;
import com.example.topmovies.data.remote.RetrofitClient;
import com.example.topmovies.data.repository.MovieRepository;
import com.example.topmovies.ui.adapter.MoviesAdapter;
import com.example.topmovies.ui.viewmodel.CarouselViewModel;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClickListener {

    @BindView(R.id.vp_movies) ViewPager2 moviesPager;
    @BindView(R.id.cnt_parent) ConstraintLayout parent;
    @BindView(R.id.progress_bar) ProgressBar progress;

    private CarouselViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewModel = new CarouselViewModel(new MovieRepository(RetrofitClient.getInstance().getMoviesApi(), MovieDatabase.getDatabase(this).movieDao()));

        moviesPager.setPageTransformer((page, position) -> {
            int offsetPx = getResources().getDimensionPixelOffset(R.dimen.page_offset);
            ViewPager2 pager = (ViewPager2) page.getParent().getParent();
            float offset = position * -offsetPx;
            if (pager.getOrientation() == ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(pager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.setTranslationX(-offset);
                } else {
                    page.setTranslationX(offset);
                }
            } else {
                page.setTranslationY(offset);
            }
        });
        moviesPager.setOffscreenPageLimit(3);

        viewModel.getPosters().observe(this, listResource -> {
            switch (listResource.getStatus()) {
                case ERROR:
                    progress.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(parent, listResource.getErrorMessage(), Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(R.string.btn_retry, v -> {
                        snackbar.dismiss();
                        viewModel.startGetPosters();
                    });
                    snackbar.show();
                    break;
                case LOADING:
                    progress.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progress.setVisibility(View.GONE);
                    moviesPager.setAdapter(new MoviesAdapter(this, listResource.getData()));
                    break;
            }
        });

        viewModel.startGetPosters();
    }

    @Override
    public void onItemClick(MoviePoster poster) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_POSTER, poster);
        startActivity(intent);
    }
}
