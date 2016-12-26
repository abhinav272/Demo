package com.android.shopr;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.Result;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.io.File;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class HomeActivity extends BaseActivity implements View.OnClickListener, ZXingScannerView.ResultHandler {

    private static final String TAG = "HomeActivity";
    Button qrScanner, qrGenerator;
    ImageView ivTemp;
    ZXingScannerView mScannerView;
    TextView scanText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpViews();
    }

    private void setUpViews() {
        qrGenerator = (Button) findViewById(R.id.btn_create_qr);
        ivTemp = (ImageView) findViewById(R.id.iv_temp);
        qrScanner = (Button) findViewById(R.id.btn_scan);
        scanText = (TextView) findViewById(R.id.tv_scan_text);
        qrScanner.setOnClickListener(this);
        qrGenerator.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_create_qr:
                Bitmap bitmap = QRCode.from("O Haan vai Kiddan ??").bitmap();
                ivTemp.setImageBitmap(bitmap);
                break;
            case R.id.btn_scan:
                mScannerView = new ZXingScannerView(this);
                setContentView(mScannerView);
                mScannerView.setResultHandler(this);
                mScannerView.startCamera();
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        mScannerView.stopCamera();
        setContentView(R.layout.activity_home);
        setUpViews();
        Log.d(TAG, "handleResult: " + result.getText());
        Log.d(TAG, "handleResult: " + result.getBarcodeFormat());

        scanText.setText(result.getText() + " ## " + result.getBarcodeFormat());
    }
}
