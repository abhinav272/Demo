package com.android.shopr.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Executor;

/**
 * Created by abhinav.sharma on 1/8/2017.
 */

public class MainThreadExecutor implements Executor {

    private static final String TAG = MainThreadExecutor.class.getSimpleName();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable runnable) {
        handler.post(runnable);
        Log.d(TAG, "execute: runnable executed on MainThread");
    }
}
