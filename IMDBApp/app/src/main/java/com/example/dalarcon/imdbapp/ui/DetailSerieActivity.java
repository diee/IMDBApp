package com.example.dalarcon.imdbapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dalarcon.imdbapp.BuildConfig;
import com.example.dalarcon.imdbapp.R;
import com.example.dalarcon.imdbapp.model.credits.Cast;
import com.example.dalarcon.imdbapp.model.discover.Serie;
import com.example.dalarcon.imdbapp.service.ImdbAPI;
import com.example.dalarcon.imdbapp.service.ImdbCreditsResponse;
import com.example.dalarcon.imdbapp.ui.fragment.CreditFragment;
import com.example.dalarcon.imdbapp.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DetailSerieActivity extends AppCompatActivity {

    public static final String INTENT_OBJECT = "object";
    public static final String INTENT_POSTER_URL = "posterUrl";
    public static final String INTENT_NAME = "name";

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
        Serie serie = extras.getParcelable(INTENT_OBJECT);

        loadResultData(serie);

        if (NetworkUtils.isWifiConnected(this)) {

            Observable<ImdbCreditsResponse> imdbCreditsResponse = ImdbAPI.getApi()
                    .getSerieCredits(serie.getId(), BuildConfig.API_KEY);

            imdbCreditsResponse.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(imdbCredits -> loadCredits(imdbCredits.getCast()));
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
        Glide.with(this)
                .load(completePath)
                .placeholder(R.mipmap.ic_launcher)
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

    private void loadResultData(Serie serie) {
        setTitle(serie.getName());
        setBackdropImage(serie.getBackdropPath());
        setImage(serie.getPosterPath());
        setReleaseDate(serie.getReleaseDate());
        setOriginalLanguage(serie.getOriginalLanguage());
        setPopularity(serie.getPopularity() + "");
        setOverview(serie.getOverview());
        setVoteAvarage(serie.getVoteAverage() + "");
        mOriginCountryTextview.setText(getString(R.string.country_text, serie.getOriginCountry()));
        mOriginCountryTextview.setVisibility(View.VISIBLE);

        mPosterImageView.setOnClickListener(view -> {
            openPoster(serie.getName(), serie.getPosterPath());
        });
    }

    private void loadCredits(List<Cast> castList) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.your_placeholder, new CreditFragment().newInstance((ArrayList<Cast>) castList));
        ft.commit();
    }

}
