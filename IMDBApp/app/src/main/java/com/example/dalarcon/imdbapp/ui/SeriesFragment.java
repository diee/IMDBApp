package com.example.dalarcon.imdbapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.dalarcon.imdbapp.BuildConfig;
import com.example.dalarcon.imdbapp.MainActivity;
import com.example.dalarcon.imdbapp.R;
import com.example.dalarcon.imdbapp.adapters.SeriesAdapter;
import com.example.dalarcon.imdbapp.model.Serie;
import com.example.dalarcon.imdbapp.service.ImdbAPI;
import com.example.dalarcon.imdbapp.service.ImdbSeriesResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SeriesFragment extends Fragment {

    private static final int PAGE_START = 1;

    @BindView(R.id.rv_series)
    RecyclerView mRvSeries;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private SeriesAdapter mSeriesAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private List<Serie> mAllSeries;

    public SeriesFragment() {
    }

    public static SeriesFragment newInstance() {
        SeriesFragment fragment = new SeriesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_series, container, false);
        ButterKnife.bind(this, view);
        mAllSeries = new ArrayList<>();
        mSeriesAdapter = new SeriesAdapter(getContext());
        mRvSeries.setAdapter(mSeriesAdapter);
        mRvSeries.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRvSeries.setLayoutManager(linearLayoutManager);

         /*
        * Load More on scroll
        */
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                callApi(page);
                /*
                * Keep view position
                **/
                int curSize = mSeriesAdapter.getItemCount();
                view.post(() -> mSeriesAdapter.notifyItemRangeInserted(curSize, mAllSeries.size() - 1));
            }
        };

        mRvSeries.addOnScrollListener(scrollListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        callApi(PAGE_START);
    }

    private void loadSeries(List<Serie> series) {
        mSeriesAdapter.addAll(series);
        mProgressBar.setVisibility(View.GONE);

    }

    private void callApi(int page) {
        if (MainActivity.isWifiConnected(getContext())) {
            ImdbAPI imdbAPI = new ImdbAPI();
            Observable<ImdbSeriesResponse> imdbSeriesResponseObservable = imdbAPI.getImdbService().
                    getSeriesResponse(BuildConfig.API_KEY, page);

            imdbSeriesResponseObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(imdbSeriesResponse -> {
                        loadSeries(imdbSeriesResponse.getSeries());
                    });
        }
    }

}
