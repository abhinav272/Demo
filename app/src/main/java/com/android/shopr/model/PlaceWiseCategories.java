package com.android.shopr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhinav on 23/03/17.
 */
public class PlaceWiseCategories implements Parcelable {
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("stores")
    @Expose
    private List<Integer> storesList = null;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Integer> getStoresList() {
        return storesList;
    }

    public void setStoresList(List<Integer> storesList) {
        this.storesList = storesList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.categoryId);
        dest.writeString(this.categoryName);
        dest.writeList(this.storesList);
    }

    public PlaceWiseCategories() {
    }

    protected PlaceWiseCategories(Parcel in) {
        this.categoryId = in.readInt();
        this.categoryName = in.readString();
        this.storesList = new ArrayList<Integer>();
        in.readList(this.storesList, Integer.class.getClassLoader());
    }

    public static final Creator<PlaceWiseCategories> CREATOR = new Creator<PlaceWiseCategories>() {
        @Override
        public PlaceWiseCategories createFromParcel(Parcel source) {
            return new PlaceWiseCategories(source);
        }

        @Override
        public PlaceWiseCategories[] newArray(int size) {
            return new PlaceWiseCategories[size];
        }
    };
}
