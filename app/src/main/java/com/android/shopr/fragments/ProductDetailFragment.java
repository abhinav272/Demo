package com.android.shopr.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shopr.HomeActivity;
import com.android.shopr.R;
import com.android.shopr.model.Product;
import com.android.shopr.utils.ShoprConstants;
import com.android.shopr.utils.Utils;
import com.squareup.picasso.Picasso;

/**
 * Created by Abhinav on 10/02/17.
 */
public class ProductDetailFragment extends BaseFragment {

    private TextView tvProductName, tvProductOriginalPrice, tvProductPriceAfterDiscount, tvProductDiscount;
    private ImageView ivProductImage;
    private Bundle argBundle;
    private Product product;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        argBundle = getArguments();
        product = argBundle.getParcelable(ShoprConstants.PRODUCT_OBJ);
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

        ((HomeActivity) getActivity()).pushTitleStack(product.getProductName());
        setUpViews();
    }

    private void setUpViews() {
        Picasso.with(getActivity()).load(product.getImageUrl()).centerCrop().fit()
                .placeholder(new ColorDrawable(Utils.getRandomBackgroundColor())).into(ivProductImage);
        tvProductName.setText(product.getProductName());
//        tvProductPriceAfterDiscount.setText(product.getPrice());
    }
}
