package com.example.dalarcon.imdbapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.dalarcon.imdbapp.BuildConfig;
import com.example.dalarcon.imdbapp.MainActivity;
import com.example.dalarcon.imdbapp.R;
import com.example.dalarcon.imdbapp.model.Movie;
import com.example.dalarcon.imdbapp.ui.DetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrador on 9/27/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private List<Movie> mMovies;
    private Context mContext;

    public MoviesAdapter(Context context) {
        this.mMovies = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View moviesView = LayoutInflater.from(mContext).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(moviesView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);

        if (movie.getPosterPath() != null) {
            holder.setImage(movie.getPosterPath());
        }
        holder.movieTitle.setText(movie.getTitle());
        holder.setReleaseDate(movie.getReleaseDate());
        holder.movieOverview.setText(movie.getOverview());
        holder.itemView.setTag(position);
    }

    public void addAll(List<Movie> movies) {
        if (movies == null)
            throw new NullPointerException("The items cannot be null");

        this.mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView moviePoster;
        public TextView movieTitle;
        public TextView movieRelease;
        public TextView movieOverview;

        public ViewHolder(View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.iv_poster);
            movieTitle = itemView.findViewById(R.id.tv_title);
            movieRelease = itemView.findViewById(R.id.tv_release_date);
            movieOverview = itemView.findViewById(R.id.tv_overview);

            itemView.setOnClickListener(view -> {
                Movie movie = mMovies.get((int) view.getTag());
                /*Intent detailMovie = new Intent(mContext, DetailActivity.class);
                detailMovie.putExtra(DetailActivity.INTENT_OBJECT, movie);
                mContext.startActivity(detailMovie);*/

                Intent detailMovie = new Intent(mContext, DetailActivity.class);
                detailMovie.putExtra(DetailActivity.INTENT_OBJECT, movie);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((MainActivity) mContext,
                                moviePoster,
                                ViewCompat.getTransitionName(moviePoster));
                mContext.startActivity(detailMovie, options.toBundle());
            });
        }

        public void setImage(String urlImage) {
            String completePath = BuildConfig.BASE_URL_IMAGE + urlImage;
            Glide.with(mContext)
                    .load(completePath)
                    .centerCrop()
                    .into(moviePoster);
        }

        public void setReleaseDate(String releaseDate) {
            movieRelease.setText(mContext.getString(R.string.release_text, releaseDate));
        }

    }
}
