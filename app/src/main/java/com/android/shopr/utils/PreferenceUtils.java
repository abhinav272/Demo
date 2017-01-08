package com.android.shopr.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.shopr.model.UserProfile;
import com.google.gson.Gson;

/**
 * Created by abhinav.sharma on 11/29/2016.
 */

public class PreferenceUtils {


    private static final String TAG = "PreferenceUtils";
    private static PreferenceUtils preferenceUtils;
    private SharedPreferences sharedPreferences;

    private PreferenceUtils() {

    }

    public static PreferenceUtils getInstance(Context context) {
        if (preferenceUtils == null)
            preferenceUtils = new PreferenceUtils();
        return preferenceUtils;
    }

    private SharedPreferences getSharedPreferences(Context context) {
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

}
