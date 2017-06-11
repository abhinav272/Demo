package com.android.shopr.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
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
    private ScrollView scrollView;
    int size = -1;
    private View.OnClickListener sizeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_size_s:
                    tvSizeS.setBackgroundResource(R.drawable.bg_circular_selected);
                    tvSizeS.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    tvSizeM.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeM.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    size = 0;
                    break;
                case R.id.tv_size_m:
                    tvSizeS.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeS.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeM.setBackgroundResource(R.drawable.bg_circular_selected);
                    tvSizeM.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    tvSizeL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    size = 1;
                    break;
                case R.id.tv_size_l:
                    tvSizeS.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeS.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeM.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeM.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeL.setBackgroundResource(R.drawable.bg_circular_selected);
                    tvSizeL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    tvSizeXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    size = 2;
                    break;
                case R.id.tv_size_xl:
                    tvSizeS.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeS.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeM.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeM.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXL.setBackgroundResource(R.drawable.bg_circular_selected);
                    tvSizeXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    tvSizeXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    size = 3;
                    break;
                case R.id.tv_size_xxl:
                    tvSizeS.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeS.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeM.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeM.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXXL.setBackgroundResource(R.drawable.bg_circular_selected);
                    tvSizeXXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    tvSizeXXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    size = 4;
                    break;
                case R.id.tv_size_xxxl:
                    tvSizeS.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeS.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeM.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeM.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDarkGrey));
                    tvSizeXXXL.setBackgroundResource(R.drawable.bg_circular_selected);
                    tvSizeXXXL.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    size = 5;
                    break;

            }
        }
    };

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
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

        flWatchProduct.setOnClickListener(this);
        flScanAndAddToBag.setOnClickListener(this);

        tvSizeS.setOnClickListener(sizeListener);
        tvSizeM.setOnClickListener(sizeListener);
        tvSizeL.setOnClickListener(sizeListener);
        tvSizeXL.setOnClickListener(sizeListener);
        tvSizeXXL.setOnClickListener(sizeListener);
        tvSizeXXXL.setOnClickListener(sizeListener);

        setUpViews();
    }

    private void setUpViews() {
        Picasso.with(getActivity()).load(product.getImageUrl()).centerCrop().fit()
                .placeholder(new ColorDrawable(Utils.getRandomBackgroundColor())).into(ivProductImage);
        tvProductName.setText(product.getProductName());
        tvProductOriginalPrice.setText(getResources().getString(R.string.ruppee_symbol) + product.getPriceBeforeDiscount());
        tvProductPriceAfterDiscount.setText(getResources().getString(R.string.ruppee_symbol) + product.getPriceAfterDiscount());
        tvProductDiscount.setText(product.getDiscount() + "Off");
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            float initialY, finalY;
            boolean isScrollingUp;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = MotionEventCompat.getActionMasked(motionEvent);

                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        initialY = motionEvent.getY();
                    case (MotionEvent.ACTION_UP):
                        finalY = motionEvent.getY();

                        if (initialY < finalY) {
                            isScrollingUp = true;
                        } else if (initialY > finalY) {
                            isScrollingUp = false;
                        }
                    default:
                }

                updateFooter(isScrollingUp);

                return false;
            }
        });
    }

    private void updateFooter(boolean flag) {
        if (!flag) {
            flWatchProduct.setVisibility(View.GONE);
            flScanAndAddToBag.setVisibility(View.GONE);
        } else {
            flWatchProduct.setVisibility(View.VISIBLE);
            flScanAndAddToBag.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_scan_and_add_to_cart:
                if (size == -1) {
                    showShortToast("Please select the Size");
                } else
                    Utils.addProductToCart(getActivity(), storeId, categoryId, storeName, storeLocation, product, size);
                break;
            case R.id.fl_watch_product:
                break;
        }
    }
}
