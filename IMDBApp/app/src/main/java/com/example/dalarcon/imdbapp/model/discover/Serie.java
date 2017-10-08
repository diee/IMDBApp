package com.example.dalarcon.imdbapp.model.discover;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by administrador on 10/4/17.
 */

public class Serie extends Result {

    @SerializedName("origin_country")
    @Expose
    private List<String> originCountry = null;

    protected Serie(Parcel in) {
        super(in);
        this.originCountry = in.readArrayList(String.class.getClassLoader());
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public static final Creator<Serie> CREATOR = new Creator<Serie>() {
        @Override
        public Serie createFromParcel(Parcel in) {
            return new Serie(in);
        }

        @Override
        public Serie[] newArray(int size) {
            return new Serie[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(originCountry);
    }

    public int describeContents() {
        return 0;
    }

}
