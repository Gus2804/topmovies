package com.example.topmovies.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.topmovies.R;
import com.example.topmovies.data.local.MovieDatabase;
import com.example.topmovies.data.local.entity.MoviePoster;
import com.example.topmovies.ui.adapter.MoviesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.vp_movies) ViewPager2 moviesPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
        List<MoviePoster> moviePosters = new ArrayList<>();
        moviePosters.add(new MoviePoster(1, "El Padrino", "/6nwJnyac60cGHo4JUtnhdTQsRsx.jpg"));
        moviePosters.add(new MoviePoster(2, "El Padrino", "/6nwJnyac60cGHo4JUtnhdTQsRsx.jpg"));
        moviePosters.add(new MoviePoster(3, "El Padrino", "/6nwJnyac60cGHo4JUtnhdTQsRsx.jpg"));
        moviesPager.setAdapter(new MoviesAdapter(this, moviePosters));

    }
}
