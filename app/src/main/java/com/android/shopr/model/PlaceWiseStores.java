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
public class PlaceWiseStores implements Parcelable {
    @SerializedName("store_id")
    @Expose
    private int storeId;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("categories")
    @Expose
    private List<StoreWiseCategories> categories = null;
    @SerializedName("img_url")
    @Expose
    private String imageUrl = null;

//    @Override
//    public boolean equals(Object obj) {
//        if (obj!=null && obj instanceof PlaceWiseStores){
//            PlaceWiseStores p = (PlaceWiseStores) obj;
//            if (storeId == p.storeId)
//                return true;
//        }
//        return false;
//    }


    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<StoreWiseCategories> getCategories() {
        return categories;
    }

    public void setCategories(List<StoreWiseCategories> categories) {
        this.categories = categories;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.storeId);
        dest.writeString(this.storeName);
        dest.writeList(this.categories);
        dest.writeString(this.imageUrl);
    }

    public PlaceWiseStores() {
    }

    protected PlaceWiseStores(Parcel in) {
        this.storeId = in.readInt();
        this.storeName = in.readString();
        this.categories = new ArrayList<StoreWiseCategories>();
        in.readList(this.categories, StoreWiseCategories.class.getClassLoader());
        this.imageUrl = in.readString();
    }

    public static final Creator<PlaceWiseStores> CREATOR = new Creator<PlaceWiseStores>() {
        @Override
        public PlaceWiseStores createFromParcel(Parcel source) {
            return new PlaceWiseStores(source);
        }

        @Override
        public PlaceWiseStores[] newArray(int size) {
            return new PlaceWiseStores[size];
        }
    };
}
