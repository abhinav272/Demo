package com.android.shopr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by abhinav.sharma on 20/01/17.
 */

public class Store {

    @SerializedName("image_url")
    @Expose
    private String imgUrl;
    @SerializedName("store_id")
    @Expose
    private int storeId;
    @SerializedName("store_name")
    @Expose
    private String storeName;

    public static class List extends ArrayList<Store> {
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

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

}
