package com.android.shopr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by abhinav.sharma on 21/01/17.
 */

public class Product {

    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("units")
    @Expose
    private int units;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }
}
