package com.android.shopr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Abhinav on 26/06/17.
 */
public class Sizes implements Parcelable {

    @SerializedName("applicable")
    @Expose
    List<String> applicable;

    @SerializedName("available")
    @Expose
    List<String> available;

    public List<String> getApplicable() {
        return applicable;
    }

    public void setApplicable(List<String> applicable) {
        this.applicable = applicable;
    }

    public List<String> getAvailable() {
        return available;
    }

    public void setAvailable(List<String> available) {
        this.available = available;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.applicable);
        dest.writeStringList(this.available);
    }

    public Sizes() {
    }

    protected Sizes(Parcel in) {
        this.applicable = in.createStringArrayList();
        this.available = in.createStringArrayList();
    }

    public static final Creator<Sizes> CREATOR = new Creator<Sizes>() {
        @Override
        public Sizes createFromParcel(Parcel source) {
            return new Sizes(source);
        }

        @Override
        public Sizes[] newArray(int size) {
            return new Sizes[size];
        }
    };
}
