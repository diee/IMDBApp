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
import com.example.dalarcon.imdbapp.adapter.SerieAdapter;
import com.example.dalarcon.imdbapp.model.discover.Serie;
import com.example.dalarcon.imdbapp.service.ImdbAPI;
import com.example.dalarcon.imdbapp.service.ImdbDiscoverResponse;
import com.example.dalarcon.imdbapp.ui.EndlessRecyclerViewScrollListener;
import com.example.dalarcon.imdbapp.util.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SerieFragment extends Fragment {

    private static final int PAGE_START = 1;

    @BindView(R.id.rv_items)
    RecyclerView mRvSeries;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private SerieAdapter mSerieAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private boolean mSortPopularity;
    private String mFilterYear;
    LinearLayoutManager mLinearLayoutManager;

    public SerieFragment() {
    }

    public static SerieFragment newInstance() {
        SerieFragment fragment = new SerieFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSerieAdapter = new SerieAdapter(getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        mRvSeries.setAdapter(mSerieAdapter);
        mRvSeries.setHasFixedSize(true);
        mRvSeries.setLayoutManager(mLinearLayoutManager);

        /*
        * Load More on scroll
        */
        scrollListener = new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                callApi(page, getSortString(mSortPopularity), mFilterYear);
            }
        };

        mRvSeries.addOnScrollListener(scrollListener);
        setupSharedPreferences();
        callApi(PAGE_START, getSortString(mSortPopularity), mFilterYear);

        return view;
    }


    private void callApi(int page, String sortPopularity, String filterYear) {
        if (NetworkUtils.isWifiConnected(getContext())) {

            Observable<ImdbDiscoverResponse<Serie>> imdbSerieResponseObservable = ImdbAPI.getApi()
                    .getSeriesResponse(BuildConfig.API_KEY, page, sortPopularity, filterYear);

            imdbSerieResponseObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(imdbDiscoverResponse -> {
                        mSerieAdapter.addAll(imdbDiscoverResponse.getResults());
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
