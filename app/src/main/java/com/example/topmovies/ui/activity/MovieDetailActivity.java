package com.example.topmovies.ui.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.example.topmovies.BuildConfig;
import com.example.topmovies.Contants;
import com.example.topmovies.R;
import com.example.topmovies.data.Status;
import com.example.topmovies.data.local.MovieDatabase;
import com.example.topmovies.data.local.entity.Movie;
import com.example.topmovies.data.local.entity.MoviePoster;
import com.example.topmovies.data.remote.RetrofitClient;
import com.example.topmovies.data.repository.MovieRepository;
import com.example.topmovies.ui.viewmodel.MovieDetailViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.time.format.TextStyle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_POSTER = "EXTRA_MOVIE_POSTER";

    /*@formatter:off*/
    @BindView(R.id.cl_parent) CoordinatorLayout parent;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.iv_movie_backdrop) ImageView backDropImage;
    @BindView(R.id.tv_original_title) TextView originalTitle;
    @BindView(R.id.tv_duration) TextView duration;
    @BindView(R.id.tv_label_overview) TextView overviewLabel;
    @BindView(R.id.tv_overview) TextView overview;
    @BindView(R.id.tv_year) TextView year;
    @BindView(R.id.tv_genre) TextView genre;
    @BindView(R.id.tv_collection) TextView collectionLabel;
    @BindView(R.id.iv_collection_poster) ImageView collectionPoster;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    /*@formatter:on*/

    private MovieDetailViewModel viewModel;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        viewModel = new MovieDetailViewModel(new MovieRepository(RetrofitClient.getInstance().getMoviesApi(), MovieDatabase.getDatabase(this).movieDao()));

        viewModel.getMovie().observe(this, movieResource -> {
            switch (movieResource.getStatus()) {
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(parent, movieResource.getErrorMessage(), Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(R.string.btn_retry, v -> {
                        snackbar.dismiss();
                        viewModel.getMovieDetails();
                    });
                    snackbar.show();
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    Movie movie = movieResource.getData();
                    Glide.with(this)
                            .load(BuildConfig.IMAGE_URL + Contants.BACKDROP_SIZE + movie.getBackdropImage())
                            .into(backDropImage);
                    setFormattedField(R.string.template_original_title, movie.getOriginalTitle(),originalTitle);
                    setFormattedField(R.string.template_duration, getString(R.string.template_minutes,movie.getDuration()),duration);
                    setFormattedField(R.string.template_year, ""+movie.getYear(),year);
                    setFormattedField(R.plurals.template_genres, movie.getGenres(),genre, movie.getGenres().split(",").length);
                    if(movie.getOverview()!=null) {
                        overview.setText(movie.getOverview());
                    }
                    else {
                        overviewLabel.setVisibility(View.GONE);
                        overview.setVisibility(View.GONE);
                    }
                    if(movie.getCollectionPoster()!=null) {
                        Glide.with(this)
                                .load(BuildConfig.IMAGE_URL + Contants.COLLECTION_SIZE + movie.getCollectionPoster())
                                .into(collectionPoster);
                    } else {
                        collectionLabel.setVisibility(View.GONE);
                        collectionPoster.setVisibility(View.GONE);
                    }
                    break;
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        });

        MoviePoster poster = getIntent().getParcelableExtra(EXTRA_MOVIE_POSTER);
        if (poster!=null) {
            if(getSupportActionBar()!=null) {
                getSupportActionBar().setTitle(poster.getTitle());
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            viewModel.setMovieId(poster.getId());
            viewModel.getMovieDetails();
        }
        else {
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFormattedField(int resId, String data, TextView tv) {
        setFormattedField(resId, data, tv, 0);
    }

    private void setFormattedField(int resId, String data, TextView tv,int quantity) {
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        String string = quantity>0 ? getResources().getQuantityString(resId, quantity, data) : getString(resId, data);
        SpannableStringBuilder builder = new SpannableStringBuilder(string);
        builder.setSpan(boldSpan,0, string.indexOf(':'), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tv.setText(builder);
    }



}
