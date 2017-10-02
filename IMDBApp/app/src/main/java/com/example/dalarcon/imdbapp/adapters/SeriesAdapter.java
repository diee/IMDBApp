package com.example.dalarcon.imdbapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dalarcon.imdbapp.BuildConfig;
import com.example.dalarcon.imdbapp.R;
import com.example.dalarcon.imdbapp.model.Serie;
import com.example.dalarcon.imdbapp.ui.DetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrador on 9/27/17.
 */

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.ViewHolder> {

    private List<Serie> mSeries;
    private Context mContext;

    public SeriesAdapter(Context context) {
        this.mSeries = new ArrayList<>();
        ;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View moviesView = LayoutInflater.from(mContext).inflate(R.layout.serie_item, parent, false);
        return new ViewHolder(moviesView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Serie serie = mSeries.get(position);

        if (serie.getPosterPath() != null) {
            holder.setImage(serie.getPosterPath());
        }
        holder.serieName.setText(serie.getName());
        holder.setOriginAndLanguage(serie.getOriginCountry(), serie.getOriginalLanguage());
        holder.setFirstAirDate(serie.getFirstAirDate());
        holder.setPopularity(serie.getPopularity() + "");
        holder.serieOverview.setText(serie.getOverview());
        holder.itemView.setTag(position);
    }

    public void addAll(List<Serie> series) {
        if (series == null)
            throw new NullPointerException("The items cannot be null");

        this.mSeries.addAll(series);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSeries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView seriePoster;
        public TextView serieName;
        public TextView serieCountry;
        public TextView serieFirstAirDate;
        public TextView seriePopularity;
        public TextView serieOverview;

        public ViewHolder(View itemView) {
            super(itemView);
            seriePoster = itemView.findViewById(R.id.iv_poster);
            serieName = itemView.findViewById(R.id.tv_name);
            serieCountry = itemView.findViewById(R.id.tv_country);
            serieFirstAirDate = itemView.findViewById(R.id.tv_first_air_date);
            seriePopularity = itemView.findViewById(R.id.tv_popularity);
            serieOverview = itemView.findViewById(R.id.tv_overview);

            itemView.setOnClickListener(view -> {
                Serie serie = mSeries.get((int) view.getTag());
                Intent detailSerie = new Intent(mContext, DetailActivity.class);
                detailSerie.putExtra(DetailActivity.INTENT_OBJECT, serie);
                mContext.startActivity(detailSerie);
            });
        }

        public void setImage(String urlImage) {
            String completePath = BuildConfig.BASE_URL_IMAGE + urlImage;
            Glide.with(mContext)
                    .load(completePath)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(seriePoster);
        }

        public void setFirstAirDate(String airDate) {
            serieFirstAirDate.setText(mContext.getString(R.string.first_air_date_text, airDate));
        }

        public void setPopularity(String popularity) {
            seriePopularity.setText(mContext.getString(R.string.popularity_text, popularity));
        }

        public void setOriginAndLanguage(List<String> countries, String lang) {
            String allCountries = TextUtils.join(" & ", countries);
            serieCountry.setText(mContext.getString(R.string.country_lang_text, allCountries, lang));

        }
    }
}
