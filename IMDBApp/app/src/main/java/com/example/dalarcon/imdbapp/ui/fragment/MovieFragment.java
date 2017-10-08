package com.example.dalarcon.imdbapp.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.dalarcon.imdbapp.BuildConfig;
import com.example.dalarcon.imdbapp.R;
import com.example.dalarcon.imdbapp.adapter.MovieAdapter;
import com.example.dalarcon.imdbapp.model.discover.Movie;
import com.example.dalarcon.imdbapp.service.ImdbAPI;
import com.example.dalarcon.imdbapp.service.ImdbDiscoverResponse;
import com.example.dalarcon.imdbapp.ui.EndlessRecyclerViewScrollListener;
import com.example.dalarcon.imdbapp.util.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieFragment extends Fragment {

    private static final int PAGE_START = 1;

    @BindView(R.id.rv_items)
    RecyclerView mRvMovies;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private MovieAdapter mMovieAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private boolean mSortPopularity;
    private String mFilterYear;
    LinearLayoutManager mLinearLayoutManager;

    public MovieFragment() {
    }

    public static MovieFragment newInstance() {
        MovieFragment fragment = new MovieFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieAdapter = new MovieAdapter(getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        mRvMovies.setAdapter(mMovieAdapter);
        mRvMovies.setHasFixedSize(true);
        mRvMovies.setLayoutManager(mLinearLayoutManager);

        /*
        * Load More on scroll
        */
        scrollListener = new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                callApi(page, getSortString(mSortPopularity), mFilterYear);
            }
        };

        mRvMovies.addOnScrollListener(scrollListener);
        setupSharedPreferences();
        callApi(PAGE_START, getSortString(mSortPopularity), mFilterYear);

        return view;
    }


    private void callApi(int page, String sortPopularity, String filterYear) {
        if (NetworkUtils.isWifiConnected(getContext())) {

            Observable<ImdbDiscoverResponse<Movie>> imdbMovieResponseObservable = ImdbAPI.getApi()
                    .getMoviesResponse(BuildConfig.API_KEY, page, sortPopularity, filterYear);

            imdbMovieResponseObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(imdbDiscoverResponse -> {
                        mMovieAdapter.addAll(imdbDiscoverResponse.getResults());
                        mProgressBar.setVisibility(View.GONE);
                    });

        }
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSortPopularity = sharedPreferences.getBoolean(getString(R.string.pref_sort_desc_key), true);
        mFilterYear = sharedPreferences.getString(getString(R.string.pref_year_key), getString(R.string.pref_year_default_value));
    }

    private String getSortString(boolean popularitySort) {
        return popularitySort ? getString(R.string.pref_sort_desc_default) : getString(R.string.pref_sort_asc_value);
    }
}
