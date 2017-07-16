package com.android.shopr.adapters.viewholders;

import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shopr.R;

/**
 * Created by Abhinav on 02/04/17.
 */
public class CartItemViewHolder extends RecyclerView.ViewHolder {

    public TextView spinnerSizePlaceHolder;
    public ImageView ivProductImage, decreseQty, increaseQty;
    public TextView productName, productPriceBeforeDiscount, productPriceAfterDiscount, productDiscount, productQuantity, removeProduct, addToWatchList;
    public AppCompatSpinner spinnerSize;

    public CartItemViewHolder(View itemView) {
        super(itemView);
        ivProductImage = (ImageView) itemView.findViewById(R.id.iv_product_image);
        decreseQty = (ImageView) itemView.findViewById(R.id.iv_decrease_qty);
        increaseQty = (ImageView) itemView.findViewById(R.id.iv_increase_qty);
        productName = (TextView) itemView.findViewById(R.id.tv_product_name);
        productPriceAfterDiscount = (TextView) itemView.findViewById(R.id.tv_product_price_after_discount);
        productPriceBeforeDiscount = (TextView) itemView.findViewById(R.id.tv_product_price_original);
        productDiscount = (TextView) itemView.findViewById(R.id.tv_product_discount);
        productQuantity = (TextView) itemView.findViewById(R.id.tv_product_quantity);
        removeProduct = (TextView) itemView.findViewById(R.id.tv_remove_product);
        addToWatchList = (TextView) itemView.findViewById(R.id.tv_move_to_watchlist);
        spinnerSize = (AppCompatSpinner) itemView.findViewById(R.id.spinner_sizes);
        spinnerSizePlaceHolder = (TextView) itemView.findViewById(R.id.tv_size_place_holder);
    }


}
