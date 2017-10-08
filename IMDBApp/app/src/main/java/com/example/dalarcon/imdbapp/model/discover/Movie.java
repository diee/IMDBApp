package com.example.dalarcon.imdbapp.model.discover;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by administrador on 10/4/17.
 */

public class Movie extends Result implements Parcelable {

    @SerializedName("adult")
    @Expose
    private boolean adult;

    public boolean isAdult() {
        return adult;
    }

    protected Movie(Parcel in) {
        super(in);
        this.adult = in.readByte() != 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (adult ? 1 : 0));
    }

    public int describeContents() {
        return 0;
    }

}
