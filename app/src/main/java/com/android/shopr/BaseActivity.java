package com.android.shopr;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by abhinav.sharma on 11/26/2016.
 */

public class BaseActivity extends AppCompatActivity {


    public void showShortToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
