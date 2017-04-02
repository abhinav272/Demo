package com.android.shopr.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.shopr.R;
import com.android.shopr.adapters.viewholders.CartItemViewHolder;
import com.android.shopr.model.Cart;
import com.android.shopr.model.CartItem;
import com.android.shopr.utils.Utils;
import com.squareup.picasso.Picasso;

/**
 * Created by Abhinav on 02/04/17.
 */
public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartItemViewHolder> {

    private Cart cart;
    private LayoutInflater inflater;
    private Context context;

    public CartRecyclerViewAdapter(Context context, Cart cart) {
        this.cart = cart;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public CartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_cart_item, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartItemViewHolder holder, int position) {
        Picasso.with(context).load(getItem(position).getImgUrl())
                .placeholder(new ColorDrawable(Utils.getRandomBackgroundColor())).fit().centerCrop().into(holder.ivProductImage);
        holder.productPriceBeforeDiscount.setText(context.getResources().getString(R.string.ruppee_symbol)
                + getItem(position).getProductPriceBeforeDiscount());
        holder.productPriceAfterDiscount.setText(context.getResources().getString(R.string.ruppee_symbol)
                + getItem(position).getProductPriceAfterDiscount());
        holder.productDiscount.setText(getItem(position).getDiscount() + "Off");
        holder.productQuantity.setText("Qty: "+ getItem(position).getProductQuantity());
        holder.productName.setText(getItem(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return cart.getCartItems().size();
    }

    private CartItem getItem(int position) {
        return cart.getCartItems().get(position);
    }
}
