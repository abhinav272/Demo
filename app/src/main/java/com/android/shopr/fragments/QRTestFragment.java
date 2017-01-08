package com.android.shopr.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.shopr.R;
import com.google.zxing.Result;

import net.glxn.qrgen.android.QRCode;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by abhinav.sharma on 12/30/2016.
 */

public class QRTestFragment extends BaseFragment implements View.OnClickListener, ZXingScannerView.ResultHandler {

    private static final String TAG = "QRTestFragment";
    Button qrScanner, qrGenerator;
    ImageView ivTemp;
    ZXingScannerView mScannerView;
    TextView scanText;
    RelativeLayout viewContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewContainer = (RelativeLayout) view.findViewById(R.id.view_container);
        mScannerView = (ZXingScannerView) view.findViewById(R.id.scanner_view);
        qrGenerator = (Button) view.findViewById(R.id.btn_create_qr);
        ivTemp = (ImageView) view.findViewById(R.id.iv_temp);
        qrScanner = (Button) view.findViewById(R.id.btn_scan);
        scanText = (TextView) view.findViewById(R.id.tv_scan_text);
        qrScanner.setOnClickListener(this);
        qrGenerator.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mScannerView != null) {
            mScannerView.stopCamera();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_create_qr:
                Bitmap bitmap = QRCode.from("Shopr custom text for testing").bitmap();
                ivTemp.setImageBitmap(bitmap);
                break;
            case R.id.btn_scan:
//                mScannerView = new ZXingScannerView(getActivity());
//                setContentView(mScannerView);
                inflateScannerView();
                mScannerView.setResultHandler(this);
                mScannerView.startCamera();
                break;
        }
    }

    private void inflateScannerView() {
        viewContainer.setVisibility(View.GONE);
        mScannerView.setVisibility(View.VISIBLE);
    }

    private void deflateScannerView() {
        mScannerView.setVisibility(View.GONE);
        viewContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void handleResult(Result result) {
        if (mScannerView != null) {
            mScannerView.stopCamera();
        }
        deflateScannerView();
        Log.d(TAG, "handleResult: " + result.getText());
        Log.d(TAG, "handleResult: " + result.getBarcodeFormat());

        scanText.setText(result.getText() + " ## " + result.getBarcodeFormat());
    }

}
