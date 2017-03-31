package com.android.shopr.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shopr.R;

/**
 * Created by Abhinav on 30/03/17.
 */
public class ProductImageViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "ProductImageViewHolder";
    public ImageView mImageView, mWatchThis;
    public TextView productName, originalPrice, priceAfterDiscount, productDiscount;

    public ProductImageViewHolder(View view) {
        super(view);
        mImageView = (ImageView) view.findViewById(R.id.iv_image);
        productName = (TextView) view.findViewById(R.id.tv_product_name);
        originalPrice = (TextView) view.findViewById(R.id.tv_product_price_original);
        priceAfterDiscount = (TextView) view.findViewById(R.id.tv_product_price_after_discount);
        productDiscount = (TextView) view.findViewById(R.id.tv_product_discount);
        mWatchThis = (ImageView) view.findViewById(R.id.iv_watch_product);
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
    }
}
