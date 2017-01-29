package com.android.shopr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.shopr.HomeActivity;
import com.android.shopr.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by abhinav.sharma on 12/30/2016.
 */

public class QRFragment extends BaseFragment implements ZXingScannerView.ResultHandler {

    private static final String TAG = "QRFragment";
    ZXingScannerView mScannerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((HomeActivity) getActivity()).pushTitleStack("Start Scanning");
        mScannerView = (ZXingScannerView) view.findViewById(R.id.scanner_view);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        ((HomeActivity) getActivity()).hideFAB();
    }

    @Override
    public void onPause() {
        super.onPause();
        deflateScannerFragment(null);
    }

    private void inflateScannerView() {

    }

    private void deflateScannerFragment(Result result) {
        if (mScannerView != null) {
            mScannerView.stopCamera();
        }
        ((HomeActivity) getActivity()).setQRResult(result);
        getActivity().getSupportFragmentManager().popBackStack();
        ((HomeActivity) getActivity()).showFAB();

    }

    @Override
    public void handleResult(Result result) {

        deflateScannerFragment(result);
        Log.d(TAG, "handleResult: " + result.getText());
        Log.d(TAG, "handleResult: " + result.getBarcodeFormat());

//        scanText.setText(result.getText() + " ## " + result.getBarcodeFormat());
    }

}
