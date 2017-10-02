package com.example.dalarcon.imdbapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.dalarcon.imdbapp.BuildConfig;
import com.example.dalarcon.imdbapp.R;
import com.example.dalarcon.imdbapp.model.Movie;
import com.example.dalarcon.imdbapp.model.Serie;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String INTENT_OBJECT = "object";
    public static final String INTENT_POSTER_URL = "posterUrl";
    public static final String INTENT_NAME = "name";

    Movie mMovie;
    Serie mSerie;

    @BindView(R.id.iv_backdrop)
    ImageView mBackdropImageView;

    @BindView(R.id.iv_poster)
    ImageView mPosterImageView;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsibleToolbarLayout;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_release_date)
    TextView mReleaseDateTextView;

    @BindView(R.id.tv_original_language)
    TextView mOriginalLanguageTextView;

    @BindView(R.id.tv_origin_country)
    TextView mOriginCountryTextview;

    @BindView(R.id.tv_public)
    TextView mPublicTextView;

    @BindView(R.id.tv_overview)
    TextView mOverviewTextView;

    @BindView(R.id.tv_popularity)
    TextView mPopularityTextView;

    @BindView(R.id.tv_vote_avarage)
    TextView mVoteAvarageTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        Object object = extras.getSerializable(INTENT_OBJECT);

        if (object instanceof Movie) {
            mMovie = (Movie) object;
            setBackdropImage(mMovie.getBackdropPath());
            setImage(mMovie.getPosterPath());
            setTitle(mMovie.getTitle());
            setReleaseDate(mMovie.getReleaseDate());
            setOriginalLanguage(mMovie.getOriginalLanguage());
            mOriginCountryTextview.setVisibility(View.GONE);
            mPublicTextView.setText(mMovie.getAdult() ?
                    getString(R.string.adult_only_text) : getString(R.string.not_adult_only_text));
            setOverview(mMovie.getOverview());
            setPopularity(mMovie.getPopularity() + "");
            setVoteAvarage(mMovie.getVoteAverage() + "");

            mPosterImageView.setOnClickListener(view -> {
                openPoster(mMovie.getTitle(), mMovie.getPosterPath());
            });

        } else if (object instanceof Serie) {
            mSerie = (Serie) object;
            setBackdropImage(mSerie.getBackdropPath());
            setImage(mSerie.getPosterPath());
            setTitle(mSerie.getName());
            setReleaseDate(mSerie.getFirstAirDate());
            setOriginalLanguage(mSerie.getOriginalLanguage());
            mOriginCountryTextview.setText(getString(R.string.country_text, mSerie.getOriginCountry()));
            mPublicTextView.setVisibility(View.GONE);
            setOverview(mSerie.getOverview());
            setPopularity(mSerie.getPopularity() + "");
            setVoteAvarage(mSerie.getVoteAverage() + "");

            mPosterImageView.setOnClickListener(view -> {
                openPoster(mSerie.getName(), mSerie.getPosterPath());
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setBackdropImage(String urlImage) {
        String completePath = BuildConfig.BASE_URL_IMAGE + urlImage;
        Glide.with(this)
                .load(completePath)
                .centerCrop()
                .into(mBackdropImageView);
    }

    private void setTitle(String title) {
        mCollapsibleToolbarLayout.setTitle(title);
        mTitleTextView.setText(title);
    }

    private void setReleaseDate(String releaseDate) {
        mReleaseDateTextView.setText(getString(R.string.release_text, releaseDate));
    }

    private void setOriginalLanguage(String originalLanguage) {
        mOriginalLanguageTextView.setText(getString(R.string.original_language_text, originalLanguage));
    }

    private void setImage(String urlImage) {
        String completePath = BuildConfig.BASE_URL_IMAGE + urlImage;
        /*Glide.with(this)
                .load(completePath)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher))
                .into(mPosterImageView);
        */

        supportPostponeEnterTransition();
        Glide.with(this)
                .load(completePath)
                .centerCrop()
                .dontAnimate()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(mPosterImageView);

    }

    private void setOverview(String overview) {
        mOverviewTextView.setText(overview);
    }

    private void setPopularity(String popularity) {
        mPopularityTextView.setText(getString(R.string.popularity_text, popularity));
    }

    private void setVoteAvarage(String voteAvarage) {
        mVoteAvarageTextView.setText(getString(R.string.vote_avarage_text, voteAvarage));
    }

    private void openPoster(String name, String posterUrl) {
        Intent posterIntent = new Intent(this, ImageActivity.class);
        posterIntent.putExtra(INTENT_POSTER_URL, BuildConfig.BASE_URL_IMAGE + posterUrl);
        posterIntent.putExtra(INTENT_NAME, name);
        startActivity(posterIntent);
    }

}
