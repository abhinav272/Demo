package com.android.shopr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by abhinav.sharma on 21/01/17.
 */

public class Product implements Parcelable {

    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("price_after_discount")
    @Expose
    private String priceAfterDiscount;
    @SerializedName("price_before_discount")
    @Expose
    private String priceBeforeDiscount;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("units")
    @Expose
    private int units;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("sizes")
    @Expose
    private Sizes sizes;

    public Sizes getSizes() {
        return sizes;
    }

    public void setSizes(Sizes sizes) {
        this.sizes = sizes;
    }

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

    public String getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(String priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public String getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }

    public void setPriceBeforeDiscount(String priceBeforeDiscount) {
        this.priceBeforeDiscount = priceBeforeDiscount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.productId);
        dest.writeString(this.productName);
        dest.writeString(this.imageUrl);
        dest.writeString(this.priceAfterDiscount);
        dest.writeString(this.priceBeforeDiscount);
        dest.writeString(this.discount);
        dest.writeInt(this.units);
        dest.writeString(this.barcode);
        dest.writeParcelable(this.sizes, flags);
    }

    public Product() {
    }

    protected Product(Parcel in) {
        this.productId = in.readInt();
        this.productName = in.readString();
        this.imageUrl = in.readString();
        this.priceAfterDiscount = in.readString();
        this.priceBeforeDiscount = in.readString();
        this.discount = in.readString();
        this.units = in.readInt();
        this.barcode = in.readString();
        this.sizes = in.readParcelable(Sizes.class.getClassLoader());
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
