package com.example.dalarcon.imdbapp.service;

import com.example.dalarcon.imdbapp.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by administrador on 9/27/17.
 */

public class ImdbAPI {

    private ImdbService imdbService;

    public ImdbAPI() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        imdbService = retrofit.create(ImdbService.class);
    }

    public ImdbService getImdbService() {
        return imdbService;
    }


}
