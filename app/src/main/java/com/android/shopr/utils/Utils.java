package com.android.shopr.utils;

import android.content.Context;
import android.util.Log;

import com.android.shopr.R;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by abhinav.sharma on 18/01/17.
 */

public class Utils {

    private static final String TAG = "Utils";

    public static void clearAllDataAndRevokeAccess(final Context context) {
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                PreferenceUtils.getInstance(context).saveUserProfile(null);
            }
        });
        Log.d(TAG, "revokeAccess: ");
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
    }

    public static int getRandomBackgroundColor() {
        List<Integer> bgColors = Arrays.asList(0xFF550C18, 0xFF443730, 0xFF786452, 0xFFF7DAD9, 0xFFFF3864,
                0xFF261447, 0xFFB6DC76, 0xFF39304A, 0xFFEAFFDA, 0xFF363020, 0xFFD6E3F8, 0xFF9D5C63,
                0xFF584B53, 0xFFFEF5EF, 0xFF197BBD, 0xFFDC758F, 0xFF00FFCD, 0xFFE7E08B, 0xFFD78A76,
                0xFFA0DDE6, 0xFFE8D6CB, 0xFFD0ADA7, 0xFFAD6A6C, 0xFFB58DB6);
//        List bgColors = Arrays.asList(context.getResources().getStringArray(R.array.bg_colors));
        Collections.shuffle(bgColors);
        int color = bgColors.get(0);
        Log.d(TAG, "getRandomBackgroundColor: "+color);
        return color;
    }
}
