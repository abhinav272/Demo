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
public class StoreWiseCategories implements Parcelable, Cloneable {
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("products")
    @Expose
    private List<Product> products;

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
        dest.writeInt(this.categoryId);
        dest.writeString(this.categoryName);
        dest.writeTypedList(this.products);
    }

    public StoreWiseCategories() {
    }

    protected StoreWiseCategories(Parcel in) {
        this.categoryId = in.readInt();
        this.categoryName = in.readString();
        this.products = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Creator<StoreWiseCategories> CREATOR = new Creator<StoreWiseCategories>() {
        @Override
        public StoreWiseCategories createFromParcel(Parcel source) {
            return new StoreWiseCategories(source);
        }

        @Override
        public StoreWiseCategories[] newArray(int size) {
            return new StoreWiseCategories[size];
        }
    };

    @Override
    public StoreWiseCategories clone() throws CloneNotSupportedException {
        super.clone();
        StoreWiseCategories storeWiseCategories = new StoreWiseCategories();
        storeWiseCategories.categoryId = categoryId;
        storeWiseCategories.categoryName = new String(categoryName);
        storeWiseCategories.products = new ArrayList<>(products);
        return storeWiseCategories;
    }
}
