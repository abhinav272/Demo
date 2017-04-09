package com.android.shopr.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.shopr.R;
import com.android.shopr.adapters.viewholders.CartItemViewHolder;
import com.android.shopr.adapters.viewholders.OptionsItemViewHolder;
import com.android.shopr.fragments.CartFragment;
import com.android.shopr.model.Cart;
import com.android.shopr.model.CartItem;
import com.android.shopr.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Abhinav on 02/04/17.
 */
public class CartRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int OPTIONS_AND_PRICE_LAYOUT = 1;
    public static final int CART_ITEM_LAYOUT = 0;
    private Cart cart;
    private LayoutInflater inflater;
    private Context context;
    private RemoveProductListener removeProductListener;

    public interface RemoveProductListener {
        void removeThisProduct(int productId);
    }

    public void setRemoveProductListener(RemoveProductListener removeProductListener) {
        this.removeProductListener = removeProductListener;
    }

    public CartRecyclerViewAdapter(Context context, Cart cart) {
        this.cart = cart;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case CART_ITEM_LAYOUT:
                view = LayoutInflater.from(context).inflate(R.layout.layout_cart_item, parent, false);
                return new CartItemViewHolder(view);
            case OPTIONS_AND_PRICE_LAYOUT:
                view = LayoutInflater.from(context).inflate(R.layout.layout_cart_options, parent, false);
                return new OptionsItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == cart.getCartItems().size()) {
            OptionsItemViewHolder optionsItemViewHolder = (OptionsItemViewHolder) holder;
            optionsItemViewHolder.tvBagTotalBeforeDiscount.setText("INR " + String.valueOf(cart.getCartTotalBeforeDiscount()));
            optionsItemViewHolder.tvBagTotalAfterDiscount.setText("INR " + String.valueOf(cart.getCartTotal()));
            String discount = String.format("%.2f", cart.getCartTotalBeforeDiscount() - cart.getCartTotal());
            optionsItemViewHolder.tvBagDiscount.setText("(-)INR " + discount);
            optionsItemViewHolder.tvAmountPayable.setText("INR " + String.valueOf(cart.getCartTotal()));
        } else {
            final CartItemViewHolder cartItemViewHolder = (CartItemViewHolder) holder;
            Picasso.with(context).load(getItem(position).getImgUrl())
                    .placeholder(new ColorDrawable(Utils.getRandomBackgroundColor())).fit().centerCrop().into(cartItemViewHolder.ivProductImage);
            cartItemViewHolder.productPriceBeforeDiscount.setText(context.getResources().getString(R.string.ruppee_symbol)
                    + getItem(position).getProductPriceBeforeDiscount());
            cartItemViewHolder.productPriceAfterDiscount.setText(context.getResources().getString(R.string.ruppee_symbol)
                    + getItem(position).getProductPriceAfterDiscount());
            cartItemViewHolder.productDiscount.setText(getItem(position).getDiscount() + "Off");
            cartItemViewHolder.productQuantity.setText("Qty: " + getItem(position).getProductQuantity());
            cartItemViewHolder.productName.setText(getItem(position).getProductName());
            cartItemViewHolder.removeProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeProductListener.removeThisProduct(getItem(position).getProductId());
                }
            });
            cartItemViewHolder.increaseQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quant = getItem(position).getProductQuantity();
                    quant += 1;
                    getItem(position).setProductQuantity(quant);
                    cartItemViewHolder.productQuantity.setText("Qty: " + getItem(position).getProductQuantity());
                }
            });
            cartItemViewHolder.decreseQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quant = getItem(position).getProductQuantity();
                    if (quant>1){
                        quant -= 1;
                        getItem(position).setProductQuantity(quant);
                        cartItemViewHolder.productQuantity.setText("Qty: " + getItem(position).getProductQuantity());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cart.getCartItems().size() + 1;
    }

    private CartItem getItem(int position) {
        if (position == cart.getCartItems().size())
            return null;
        return cart.getCartItems().get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == cart.getCartItems().size())
            return OPTIONS_AND_PRICE_LAYOUT;
        else return CART_ITEM_LAYOUT;
    }
}
