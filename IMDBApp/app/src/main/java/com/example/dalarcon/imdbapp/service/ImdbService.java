package com.example.dalarcon.imdbapp.service;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by administrador on 9/27/17.
 */

public interface ImdbService {

    @GET("discover/movie?language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false")
    Observable<ImdbMovieResponse> getMoviesResponse(@Query("api_key") String key, @Query("page") int page);

    @GET("discover/tv?language=en-US&sort_by=popularity.desc&timezone=America%2FNew_York&include_null_first_air_dates=false")
    Observable<ImdbSeriesResponse> getSeriesResponse(@Query("api_key") String key, @Query("page") int page);
}
