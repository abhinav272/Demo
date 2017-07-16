package com.android.shopr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shopr.R;
import com.android.shopr.model.Cart;
import com.android.shopr.model.CartItem;
import com.squareup.picasso.Picasso;

/**
 * Created by Abhinav on 01/07/17.
 */
public class CartBaseAdapter extends BaseAdapter {

    private Cart cart;
    private Context context;
    private LayoutInflater inflater;

    public CartBaseAdapter(Context context, Cart cart) {
        this.cart = cart;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cart.getCartItems().size();
    }

    @Override
    public CartItem getItem(int position) {
        return cart.getCartItems().get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (convertView == null) {
            view = inflater.inflate(R.layout.layout_final_cart_item, parent, false);
        } else {
            view = convertView;
        }


        Picasso.with(context).load(getItem(position).getImgUrl()).fit().centerCrop().into((ImageView) view.findViewById(R.id.iv_product_image));
        ((TextView) view.findViewById(R.id.tv_product_name)).setText(getItem(position).getProductName());
        if (getItem(position).getSizes().getApplicable().size() > 0)
            ((TextView) view.findViewById(R.id.tv_size)).setText("Size: " + getItem(position).getSizes().getApplicable().get(getItem(position).getSize()));
        else ((TextView) view.findViewById(R.id.tv_size)).setText("Size: N/A");
        ((TextView) view.findViewById(R.id.tv_qty)).setText("Qyt: " + getItem(position).getProductQuantity());
        double total = getItem(position).getProductPriceAfterDiscount() * getItem(position).getProductQuantity();
        ((TextView) view.findViewById(R.id.tv_total)).setText(getItem(position).getProductQuantity() + " X "
                + getItem(position).getProductPriceAfterDiscount() + " = "
                + String.format("%.2f", total));

        return view;
    }
}
