package com.android.shopr;

import android.support.v7.app.AppCompatActivity;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by abhinav.sharma on 11/26/2016.
 */

public class BaseActivity extends AppCompatActivity {
    public void showScannerView(ZXingScannerView mScannerView) {
        setContentView(mScannerView);
    }
}
