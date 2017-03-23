package com.android.shopr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Abhinav on 23/03/17.
 */
public class PlaceWiseCategoriesStores {
    @SerializedName("categories")
    @Expose
    private List<PlaceWiseCategories> categories = null;
    @SerializedName("stores")
    @Expose
    private List<PlaceWiseStores> stores = null;

    public List<PlaceWiseCategories> getCategories() {
        return categories;
    }

    public void setCategories(List<PlaceWiseCategories> categories) {
        this.categories = categories;
    }

    public List<PlaceWiseStores> getStores() {
        return stores;
    }

    public void setStores(List<PlaceWiseStores> stores) {
        this.stores = stores;
    }
}
