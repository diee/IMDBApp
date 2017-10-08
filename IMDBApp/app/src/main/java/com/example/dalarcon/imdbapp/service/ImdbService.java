package com.example.dalarcon.imdbapp.service;

import com.example.dalarcon.imdbapp.model.discover.Movie;
import com.example.dalarcon.imdbapp.model.discover.Serie;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by administrador on 9/27/17.
 */

public interface ImdbService {

    @GET("discover/movie?language=en-US&include_adult=false&include_video=false")
    Observable<ImdbDiscoverResponse<Movie>> getMoviesResponse(@Query("api_key") String key, @Query("page") int page, @Query("sort_by") String sortby, @Query("primary_release_year") String year);

    @GET("movie/{movie}/credits")
    Observable<ImdbCreditsResponse> getMovieCredits(@Path("movie") int movieId, @Query("api_key") String key);

    @GET("discover/tv?language=en-US&sort_by=popularity.desc&timezone=America%2FNew_York&include_null_first_air_dates=false")
    Observable<ImdbDiscoverResponse<Serie>> getSeriesResponse(@Query("api_key") String key, @Query("page") int page, @Query("sort_by") String sortby, @Query("first_air_date_year") String year);

    @GET("tv/{serie}/credits")
    Observable<ImdbCreditsResponse> getSerieCredits(@Path("serie") int serieId, @Query("api_key") String key);
}