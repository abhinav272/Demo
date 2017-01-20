package com.android.shopr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhinav.sharma on 20/01/17.
 */

public class Category {

    @SerializedName("active")
    @Expose
    private int active;
    @SerializedName("categoryId")
    @Expose
    private int categoryId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("created")
    @Expose
    private int created;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;
    @SerializedName("modified")
    @Expose
    private int modified;
    @SerializedName("storeId")
    @Expose
    private int storeId;

    public static class List extends ArrayList<Category> {
    }


    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

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

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
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

}
