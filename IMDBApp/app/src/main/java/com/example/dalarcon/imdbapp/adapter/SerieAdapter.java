package com.example.dalarcon.imdbapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dalarcon.imdbapp.BuildConfig;
import com.example.dalarcon.imdbapp.R;
import com.example.dalarcon.imdbapp.model.discover.Serie;
import com.example.dalarcon.imdbapp.ui.DetailSerieActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrador on 9/27/17.
 */

public class SerieAdapter extends RecyclerView.Adapter<SerieAdapter.ViewHolder> {

    private List<Serie> mSeries;
    private Context mContext;

    public SerieAdapter(Context context) {
        this.mSeries = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View moviesView = LayoutInflater.from(mContext).inflate(R.layout.result_item, parent, false);
        return new ViewHolder(moviesView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Serie serie = mSeries.get(position);
        if (serie != null) {
            if (serie.getPosterPath() != null) {
                holder.setImage(serie.getPosterPath());
            }
            holder.resultName.setText(serie.getName());
            holder.setReleaseDate(serie.getReleaseDate());
            holder.resultOverview.setText(serie.getOverview());
            holder.itemView.setTag(position);
        }
    }

    public void addAll(List<Serie> results) {
        if (results == null)
            throw new NullPointerException("The items cannot be null");

        this.mSeries.addAll(results);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSeries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView resultPoster;
        public TextView resultName;
        public TextView resultReleaseDate;
        public TextView resultOverview;

        public ViewHolder(View itemView) {
            super(itemView);
            resultPoster = itemView.findViewById(R.id.iv_poster);
            resultName = itemView.findViewById(R.id.tv_name);
            resultReleaseDate = itemView.findViewById(R.id.tv_release_date);
            resultOverview = itemView.findViewById(R.id.tv_overview);

            itemView.setOnClickListener(view -> {
                Serie result = mSeries.get((int) view.getTag());
                Intent detail = new Intent(mContext, DetailSerieActivity.class);
                detail.putExtra(DetailSerieActivity.INTENT_OBJECT, result);
                mContext.startActivity(detail);
            });
        }

        public void setImage(String urlImage) {
            String completePath = BuildConfig.BASE_URL_IMAGE + urlImage;
            Glide.with(mContext)
                    .load(completePath)
                    .placeholder(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(resultPoster);
        }

        public void setReleaseDate(String releaseDate) {
            resultReleaseDate.setText(mContext.getString(R.string.release_text, releaseDate));
        }
    }
}
