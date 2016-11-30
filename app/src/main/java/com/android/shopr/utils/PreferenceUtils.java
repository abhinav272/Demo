package com.android.shopr.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abhinav.sharma on 11/29/2016.
 */

public class PreferenceUtils {

    public static final String APP_PREFERENCE = "shopr_preference";
    private static PreferenceUtils preferenceUtils;
    private SharedPreferences sharedPreferences;

    private PreferenceUtils() {

    }

    private SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
        }

        return sharedPreferences;
    }

    public PreferenceUtils getInstance(Context context) {
        if (preferenceUtils == null)
            preferenceUtils = new PreferenceUtils();
        return preferenceUtils;
    }

}
