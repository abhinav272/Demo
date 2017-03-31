package com.android.shopr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by abhinav.sharma on 21/01/17.
 */

public class CategoryWiseProducts implements Parcelable {
    @SerializedName("store_id")
    @Expose
    private int storeId;
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;
    @SerializedName("category_name")
    @Expose
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.storeId);
        dest.writeInt(this.categoryId);
        dest.writeTypedList(this.products);
        dest.writeString(this.categoryName);
    }

    public CategoryWiseProducts() {
    }

    protected CategoryWiseProducts(Parcel in) {
        this.storeId = in.readInt();
        this.categoryId = in.readInt();
        this.products = in.createTypedArrayList(Product.CREATOR);
        this.categoryName = in.readString();
    }

    public static final Creator<CategoryWiseProducts> CREATOR = new Creator<CategoryWiseProducts>() {
        @Override
        public CategoryWiseProducts createFromParcel(Parcel source) {
            return new CategoryWiseProducts(source);
        }

        @Override
        public CategoryWiseProducts[] newArray(int size) {
            return new CategoryWiseProducts[size];
        }
    };
}
