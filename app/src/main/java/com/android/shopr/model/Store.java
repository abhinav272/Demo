package com.android.shopr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by abhinav.sharma on 20/01/17.
 */

public class Store {

    @SerializedName("active")
    @Expose
    private int active;
    @SerializedName("created")
    @Expose
    private long created;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;
    @SerializedName("modified")
    @Expose
    private int modified;
    @SerializedName("storeId")
    @Expose
    private int storeId;
    @SerializedName("storeName")
    @Expose
    private String storeName;

    public static class List extends ArrayList<Store> {
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getModified() {
        return modified;
    }

    public void setModified(int modified) {
        this.modified = modified;
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
