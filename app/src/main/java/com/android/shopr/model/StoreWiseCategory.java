package com.android.shopr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by abhinav.sharma on 21/01/17.
 */

public class StoreWiseCategory {

    @SerializedName("store_id")
    @Expose
    private int storeId;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("store_name")
    @Expose
    private String storeName;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
