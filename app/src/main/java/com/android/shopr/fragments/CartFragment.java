package com.android.shopr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.shopr.R;

/**
 * Created by Abhinav on 02/04/17.
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {

    private ImageView ivBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        getActivity().finish();
    }
}
