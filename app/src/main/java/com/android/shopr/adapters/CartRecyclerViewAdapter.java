package com.android.shopr.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.shopr.R;
import com.android.shopr.adapters.viewholders.CartItemViewHolder;
import com.android.shopr.adapters.viewholders.OptionsItemViewHolder;
import com.android.shopr.fragments.CartFragment;
import com.android.shopr.model.Cart;
import com.android.shopr.model.CartItem;
import com.android.shopr.utils.ExecutorSupplier;
import com.android.shopr.utils.PreferenceUtils;
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
    private ProductListener productListener;

    public interface ProductListener {
        void removeThisProduct(CartItem cartItem);
        void updateCartOnUI();
    }

    public void setProductListener(ProductListener productListener) {
        this.productListener = productListener;
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
            optionsItemViewHolder.tvBagTotalBeforeDiscount.setText("INR " + String.format("%.2f", cart.getCartTotalBeforeDiscount()));
            optionsItemViewHolder.tvBagTotalAfterDiscount.setText("INR " + String.format("%.2f", cart.getCartTotal()));
            String discount = String.format("%.2f", cart.getCartTotalBeforeDiscount() - cart.getCartTotal());
            optionsItemViewHolder.tvBagDiscount.setText("(-)INR " + discount);
            optionsItemViewHolder.tvTax.setText("INR " + String.format("%.2f", (cart.getCartTotal() * 0.10)));
            optionsItemViewHolder.tvAmountPayable.setText("INR " + String.format("%.2f", cart.getCartTotal() * 1.10));

        } else {
            final CartItemViewHolder cartItemViewHolder = (CartItemViewHolder) holder;
            Picasso.with(context).load(getItem(position).getImgUrl())
                    .placeholder(new ColorDrawable(Utils.getRandomBackgroundColor())).fit().centerCrop().into(cartItemViewHolder.ivProductImage);
            setProductPrice(position, cartItemViewHolder);
            cartItemViewHolder.productDiscount.setText(getItem(position).getDiscount() + "Off");
            cartItemViewHolder.productName.setText(getItem(position).getProductName());
            cartItemViewHolder.removeProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productListener.removeThisProduct(getItem(position));
                }
            });
            cartItemViewHolder.increaseQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quant = getItem(position).getProductQuantity();
                    quant += 1;
                    getItem(position).setProductQuantity(quant);
                    updatePrice();
                    setProductPrice(position, cartItemViewHolder);
                    cartItemViewHolder.productQuantity.setText("Qty: " + getItem(position).getProductQuantity());
                }
            });
            cartItemViewHolder.decreseQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quant = getItem(position).getProductQuantity();
                    if (quant > 1) {
                        quant -= 1;
                        getItem(position).setProductQuantity(quant);
                        updatePrice();
                        setProductPrice(position, cartItemViewHolder);
                        cartItemViewHolder.productQuantity.setText("Qty: " + getItem(position).getProductQuantity());
                    }
                }
            });
            if (getItem(position).getSizes().getApplicable().size() > 0){
                cartItemViewHolder.spinnerSize.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, getItem(position).getSizes().getAvailable()));
                int index = getItem(position).getSizes().getAvailable().indexOf(getItem(position).getSizes().getApplicable().get(getItem(position).getSize()));
                cartItemViewHolder.spinnerSize.setSelection(index);
                cartItemViewHolder.spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!getItem(position).getSizes().getAvailable().contains(((AppCompatTextView) view).getText().toString())){
                            Toast.makeText(context, "Size not available", Toast.LENGTH_SHORT).show();
                        } else {
                            int index = getItem(position).getSizes().getApplicable().indexOf(((AppCompatTextView) view).getText().toString());
                            getItem(position).setSize(index);
                            ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    PreferenceUtils.getInstance(context).saveUserCart(cart);
                                }
                            });
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            } else {
                cartItemViewHolder.spinnerSize.setVisibility(View.GONE);
                cartItemViewHolder.spinnerSizePlaceHolder.setText("Size: N/A");
            }
        }
    }

    private void setProductPrice(int position, CartItemViewHolder cartItemViewHolder) {
        cartItemViewHolder.productPriceBeforeDiscount.setText(context.getResources().getString(R.string.ruppee_symbol)
                + String.format("%.2f", getItem(position).getProductPriceBeforeDiscount() * getItem(position).getProductQuantity()));
        cartItemViewHolder.productPriceAfterDiscount.setText(context.getResources().getString(R.string.ruppee_symbol)
                + String.format("%.2f", getItem(position).getProductPriceAfterDiscount() * getItem(position).getProductQuantity()));
        cartItemViewHolder.productQuantity.setText("Qty: " + getItem(position).getProductQuantity());
    }

    private void updatePrice() {
        double totalAfterDiscount = 0.0f;
        int items = 0;
        for (CartItem item : cart.getCartItems()) {
            totalAfterDiscount += item.getProductPriceAfterDiscount() * item.getProductQuantity();
            items += item.getProductQuantity();
        }
        cart.setCartTotal(totalAfterDiscount);
        cart.updateCartTotal();
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                PreferenceUtils.getInstance(context).saveUserCart(cart);
            }
        });
        notifyDataSetChanged();
        productListener.updateCartOnUI();
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
