package com.example.dalarcon.imdbapp.model.credits;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by administrador on 10/6/17.
 */

public class CreditPerson implements Parcelable{

    @SerializedName("credit_id")
    @Expose
    private String creditId;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    protected CreditPerson(Parcel in) {
        creditId = in.readString();
        id = in.readInt();
        name = in.readString();
        profilePath = in.readString();
    }

    public static final Creator<CreditPerson> CREATOR = new Creator<CreditPerson>() {
        @Override
        public CreditPerson createFromParcel(Parcel in) {
            return new CreditPerson(in);
        }

        @Override
        public CreditPerson[] newArray(int size) {
            return new CreditPerson[size];
        }
    };

    public String getCreditId() {
        return creditId;
    }

    public Integer getGender() {
        return gender;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(creditId);
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(profilePath);
    }
}
