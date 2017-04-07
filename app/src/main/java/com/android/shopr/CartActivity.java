package com.android.shopr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.android.shopr.fragments.CartFragment;
import com.android.shopr.model.Cart;
import com.android.shopr.model.CartItem;
import com.android.shopr.utils.PreferenceUtils;

/**
 * Created by Abhinav on 01/04/17.
 */
public class CartActivity extends BaseActivity {

    private FragmentManager mFragmentManager;
    private Cart cart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initSetup();
        initCartFragment();
    }

    private void initSetup() {
        mFragmentManager = getSupportFragmentManager();
        cart = PreferenceUtils.getInstance(this).getUserCart();
    }

    private void initCartFragment() {
        mFragmentManager.beginTransaction()
                .add(R.id.frame_container, new CartFragment(), CartFragment.class.getSimpleName())
                .commit();
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
