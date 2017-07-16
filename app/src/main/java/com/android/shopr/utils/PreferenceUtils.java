package com.android.shopr.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.shopr.model.AddedCartResponse;
import com.android.shopr.model.Cart;
import com.android.shopr.model.CartItem;
import com.android.shopr.model.UserProfile;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by abhinav.sharma on 11/29/2016.
 */

public class PreferenceUtils {


    private static final String TAG = "PreferenceUtils";
    private static PreferenceUtils preferenceUtils;
    private static SharedPreferences sharedPreferences;

    private PreferenceUtils() {

    }

    public static PreferenceUtils getInstance(Context context) {
        if (preferenceUtils == null) {
            preferenceUtils = new PreferenceUtils();
            sharedPreferences = getSharedPreferences(context);
        }
        return preferenceUtils;
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(ShoprConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
        }

        return sharedPreferences;
    }

    public void saveUserProfile(UserProfile userProfile) {
        Gson gson = new Gson();
        String json = gson.toJson(userProfile);
        sharedPreferences.edit().putString(ShoprConstants.USER_PROFILE_PREFS_KEYS, json).apply();
        Log.d(TAG, "saveUserProfile: profile saved");
        Log.d(TAG, "saveUserProfile: on thread " + Thread.currentThread().getName());
    }

    public UserProfile getUserProfile() {
        String string = sharedPreferences.getString(ShoprConstants.USER_PROFILE_PREFS_KEYS, null);
        Gson gson = new Gson();
        return gson.fromJson(string, UserProfile.class);
    }

    public Cart getUserCart() {
        String string = sharedPreferences.getString(ShoprConstants.CART, null);
        Gson gson = new Gson();
        Cart cart = gson.fromJson(string, Cart.class);
        if (cart == null) {
            cart = new Cart();
            cart.setCartItems(new ArrayList<CartItem>());
        }
        return cart;
    }

    public void saveUserCart(Cart cart) {
        Gson gson = new Gson();
        String json = gson.toJson(cart);
        sharedPreferences.edit().putString(ShoprConstants.CART, json).apply();
    }

    public void saveAwaitingCart(AddedCartResponse body) {
        Gson gson = new Gson();
        String json = gson.toJson(body);
        sharedPreferences.edit().putString(ShoprConstants.AWAITING_CART, json).apply();
    }

    public AddedCartResponse getAwaitingCart() {
        String string = sharedPreferences.getString(ShoprConstants.AWAITING_CART, null);
        if (string!=null){
            return new Gson().fromJson(string, AddedCartResponse.class);
        }
        return null;
    }
}
