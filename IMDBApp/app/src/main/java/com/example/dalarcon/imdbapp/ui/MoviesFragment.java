package com.example.dalarcon.imdbapp.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dalarcon.imdbapp.BuildConfig;
import com.example.dalarcon.imdbapp.MainActivity;
import com.example.dalarcon.imdbapp.R;
import com.example.dalarcon.imdbapp.adapters.MoviesAdapter;
import com.example.dalarcon.imdbapp.model.Movie;
import com.example.dalarcon.imdbapp.service.ImdbAPI;
import com.example.dalarcon.imdbapp.service.ImdbMovieResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MoviesFragment extends Fragment {

    private static final int PAGE_START = 1;
    @BindView(R.id.rv_movies)
    RecyclerView mRvMovies;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private MoviesAdapter mMoviesAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private List<Movie> mAllMovies;

    public MoviesFragment() {
    }

    public static MoviesFragment newInstance() {
        MoviesFragment fragment = new MoviesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);
        mAllMovies = new ArrayList<>();
        mMoviesAdapter = new MoviesAdapter(getContext());
        mRvMovies.setAdapter(mMoviesAdapter);
        mRvMovies.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRvMovies.setLayoutManager(linearLayoutManager);

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
                int curSize = mMoviesAdapter.getItemCount();
                view.post(() -> mMoviesAdapter.notifyItemRangeInserted(curSize, mAllMovies.size() - 1));
            }
        };

        mRvMovies.addOnScrollListener(scrollListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        callApi(PAGE_START);
    }

    private void loadMovies(List<Movie> movies) {
        mMoviesAdapter.addAll(movies);
        mProgressBar.setVisibility(View.GONE);
    }

    private void callApi(int page) {
        if (MainActivity.isWifiConnected(getContext())) {
            ImdbAPI imdbAPI = new ImdbAPI();
            Observable<ImdbMovieResponse> imdbResponseObservable = imdbAPI.getImdbService().
                    getMoviesResponse(BuildConfig.API_KEY, page);

            imdbResponseObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(imdbMovieResponse -> {
                        loadMovies(imdbMovieResponse.getMovies());
                    });
        }
    }
}
