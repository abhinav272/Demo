package com.android.shopr.model;

/**
 * Created by Abhinav on 02/04/17.
 */
public class CartItem {

    private int storeId;
    private int categoryId;
    private int productId;
    private int productQuantity;
    private double productPriceAfterDiscount;
    private double productPriceBeforeDiscount;
    private String productName;
    private String discount;
    private String imgUrl;
    private String storeName;
    private String locationName;
    private int size;

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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getProductPriceAfterDiscount() {
        return productPriceAfterDiscount;
    }

    public void setProductPriceAfterDiscount(double productPriceAfterDiscount) {
        this.productPriceAfterDiscount = productPriceAfterDiscount;
    }

    public double getProductPriceBeforeDiscount() {
        return productPriceBeforeDiscount;
    }

    public void setProductPriceBeforeDiscount(double productPriceBeforeDiscount) {
        this.productPriceBeforeDiscount = productPriceBeforeDiscount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof CartItem){
            if (productId == ((CartItem) obj).productId
                    && categoryId == ((CartItem) obj).categoryId
                    && storeId == ((CartItem) obj).storeId && size == ((CartItem) obj).size)
                return true;
        }
        return false;
    }
}
