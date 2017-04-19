package com.android.shopr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhinav on 19/04/17.
 */
public class UserCart {
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("cart")
    @Expose
    private Cart cart;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
