package com.example.dalarcon.imdbapp.model.discover;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by administrador on 10/2/17.
 */

public class Result implements Parcelable {

    @SerializedName(value = "name", alternate = "title")
    @Expose
    private String name;
    @SerializedName("popularity")
    @Expose
    private double popularity;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("vote_average")
    @Expose
    private double voteAverage;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName(value = "release_date", alternate = "first_air_date")
    @Expose
    private String releaseDate;

    public String getName() {
        return name;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public int getId() {
        return id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Result(){}

    protected Result(Parcel in) {
        name = in.readString();
        popularity = in.readDouble();
        backdropPath = in.readString();
        originalLanguage = in.readString();
        id = in.readInt();
        voteAverage = in.readDouble();
        overview = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(popularity);
        parcel.writeString(backdropPath);
        parcel.writeString(originalLanguage);
        parcel.writeInt(id);
        parcel.writeDouble(voteAverage);
        parcel.writeString(overview);
        parcel.writeString(posterPath);
        parcel.writeString(releaseDate);
    }
}
