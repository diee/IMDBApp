package com.example.dalarcon.imdbapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dalarcon.imdbapp.BuildConfig;
import com.example.dalarcon.imdbapp.R;
import com.example.dalarcon.imdbapp.model.credits.Cast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrador on 10/6/17.
 */

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private List<Cast> mCasts;
    private Context mContext;

    public CastAdapter(Context context) {
        this.mCasts = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View castView = LayoutInflater.from(mContext).inflate(R.layout.cast_item, parent, false);
        return new ViewHolder(castView);
    }

    @Override
    public void onBindViewHolder(CastAdapter.ViewHolder holder, int position) {
        Cast cast = mCasts.get(position);
        if (cast != null) {
            if (cast.getProfilePath() != null) {
                holder.setImage(cast.getProfilePath());
            }
            holder.castName.setText(cast.getName());
            holder.castChar.setText(cast.getCharacter());
        }
    }

    public void addAll(List<Cast> results) {
        if (results == null)
            throw new NullPointerException("The items cannot be null");

        this.mCasts.addAll(results);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCasts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView castPoster;
        public TextView castName;
        public TextView castChar;

        public ViewHolder(View itemView) {
            super(itemView);
            castPoster = itemView.findViewById(R.id.iv_poster);
            castName = itemView.findViewById(R.id.tv_name);
            castChar = itemView.findViewById(R.id.tv_character);
        }

        public void setImage(String urlImage) {
            String completePath = BuildConfig.BASE_URL_IMAGE + urlImage;
            Glide.with(mContext)
                    .load(completePath)
                    .placeholder(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(castPoster);
        }
    }
}
