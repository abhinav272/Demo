package com.android.shopr.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.shopr.HomeActivity;
import com.android.shopr.ProductDetailActivity;
import com.android.shopr.R;
import com.android.shopr.model.Cart;
import com.android.shopr.model.CartItem;
import com.android.shopr.model.Product;
import com.android.shopr.utils.PreferenceUtils;
import com.android.shopr.utils.ShoprConstants;
import com.android.shopr.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhinav on 10/02/17.
 */
public class ProductDetailFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvProductName, tvProductOriginalPrice, tvProductPriceAfterDiscount, tvProductDiscount, tvSizeChart, tvSizeS, tvSizeM, tvSizeL, tvSizeXL, tvSizeXXL, tvSizeXXXL;
    private ImageView ivProductImage;
    private Bundle argBundle;
    private Product product;
    private FrameLayout flScanAndAddToBag, flWatchProduct;
    private int storeId, categoryId;
    private String storeName, storeLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        argBundle = getArguments();
        product = argBundle.getParcelable(ShoprConstants.PRODUCT_OBJ);
        storeId = ((ProductDetailActivity) getActivity()).getStoreId();
        categoryId = ((ProductDetailActivity) getActivity()).getCategoryId();
        storeName = ((ProductDetailActivity) getActivity()).getStoreName();
        storeLocation = ((ProductDetailActivity) getActivity()).getLocationName();
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
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
        tvProductDiscount = (TextView) view.findViewById(R.id.tv_product_discount);
        tvProductName = (TextView) view.findViewById(R.id.tv_product_name);
        tvProductOriginalPrice = (TextView) view.findViewById(R.id.tv_product_price_original);
        tvProductPriceAfterDiscount = (TextView) view.findViewById(R.id.tv_product_price_after_discount);
        ivProductImage = (ImageView) view.findViewById(R.id.iv_product_image);
        tvSizeChart = (TextView) view.findViewById(R.id.tv_size_chart);
        tvSizeS = (TextView) view.findViewById(R.id.tv_size_s);
        tvSizeM = (TextView) view.findViewById(R.id.tv_size_m);
        tvSizeL = (TextView) view.findViewById(R.id.tv_size_l);
        tvSizeXL = (TextView) view.findViewById(R.id.tv_size_xl);
        tvSizeXXL = (TextView) view.findViewById(R.id.tv_size_xxl);
        tvSizeXXXL = (TextView) view.findViewById(R.id.tv_size_xxxl);
        flWatchProduct = (FrameLayout) view.findViewById(R.id.fl_watch_product);
        flScanAndAddToBag = (FrameLayout) view.findViewById(R.id.fl_scan_and_add_to_cart);

        flWatchProduct.setOnClickListener(this);
        flScanAndAddToBag.setOnClickListener(this);

        setUpViews();
    }

    private void setUpViews() {
        Picasso.with(getActivity()).load(product.getImageUrl()).centerCrop().fit()
                .placeholder(new ColorDrawable(Utils.getRandomBackgroundColor())).into(ivProductImage);
        tvProductName.setText(product.getProductName());
        tvProductOriginalPrice.setText(getResources().getString(R.string.ruppee_symbol) + product.getPriceBeforeDiscount());
        tvProductPriceAfterDiscount.setText(getResources().getString(R.string.ruppee_symbol) + product.getPriceAfterDiscount());
        tvProductDiscount.setText(product.getDiscount() + "Off");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_scan_and_add_to_cart:
                Utils.addProductToCart(getActivity(), storeId, categoryId, storeName, storeLocation, product);
                break;
            case R.id.fl_watch_product:
                break;
        }
    }
}
