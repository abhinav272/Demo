package com.android.shopr.utils;

import android.content.Context;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

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
}
