package com.android.shopr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.shopr.HomeActivity;
import com.android.shopr.R;
import com.android.shopr.StoresDetailActivity;
import com.android.shopr.utils.Utils;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by abhinav.sharma on 12/30/2016.
 */

public class QRFragment extends BaseFragment implements ZXingScannerView.ResultHandler, View.OnClickListener {

    private static final String TAG = "QRFragment";
    ZXingScannerView mScannerView;
    ImageView ivSearchByBarcode;
    EditText etBarcode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        ivSearchByBarcode = (ImageView) view.findViewById(R.id.iv_search_product_by_barcode);
        etBarcode = (EditText) view.findViewById(R.id.et_search_product);
        mScannerView = (ZXingScannerView) view.findViewById(R.id.scanner_view);
        ivSearchByBarcode.setOnClickListener(this);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        deflateScannerFragment("pause");
    }

    private void inflateScannerView() {

    }

    private void deflateScannerFragment(Result result) {
        if (mScannerView != null) {
            mScannerView.stopCamera();
        }
        ((StoresDetailActivity) getActivity()).setQRResult(result);
        getActivity().getSupportFragmentManager().popBackStack();
//        ((HomeActivity) getActivity()).showBNV();

    }

    private void deflateScannerFragment(String barcode) {
        if (mScannerView != null) {
            mScannerView.stopCamera();
        }
        if (!barcode.equals("pause")){
            ((StoresDetailActivity) getActivity()).setQRResult(barcode);
        }
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void handleResult(Result result) {

        deflateScannerFragment(result);
        Log.d(TAG, "handleResult: " + result.getText());
        Log.d(TAG, "handleResult: " + result.getBarcodeFormat());

//        scanText.setText(result.getText() + " ## " + result.getBarcodeFormat());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search_product_by_barcode:
                if (etBarcode.getText().toString().trim().length() > 1) {
                    Utils.hideKeyboard(getActivity());
                    deflateScannerFragment(etBarcode.getText().toString().trim());
                }
                break;
        }
    }
}
