package com.android.shopr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.shopr.api.ShoprAPIClient;
import com.android.shopr.fragments.CartFragment;
import com.android.shopr.fragments.ConfirmationFragment;
import com.android.shopr.model.AddedCartResponse;
import com.android.shopr.model.Cart;
import com.android.shopr.model.UserCart;
import com.android.shopr.utils.ExecutorSupplier;
import com.android.shopr.utils.PreferenceUtils;
import com.android.shopr.utils.Utils;

import net.glxn.qrgen.android.QRCode;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Abhinav on 01/04/17.
 */
public class CartActivity extends BaseActivity implements Callback<AddedCartResponse> {

    private FragmentManager mFragmentManager;
    private Cart cart;
    private RelativeLayout rlContainer;
    private ImageView ivCartQr;
    private TextView tvCartId, tvTimeStamp, tvOrderStatus;
    private FrameLayout flContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initSetup();
        if (PreferenceUtils.getInstance(this).getAwaitingCart()!=null){
            setupConfirmationScreen(PreferenceUtils.getInstance(this).getAwaitingCart());
        } else initCartFragment();
    }

    private void initSetup() {
        mFragmentManager = getSupportFragmentManager();
        cart = PreferenceUtils.getInstance(this).getUserCart();
        rlContainer = (RelativeLayout) findViewById(R.id.rl_container);
        flContainer = (FrameLayout) findViewById(R.id.frame_container);
        ivCartQr = (ImageView) findViewById(R.id.iv_cart_qr);
        tvCartId = (TextView) findViewById(R.id.tv_cart_id);
        tvTimeStamp = (TextView) findViewById(R.id.tv_time_stamp);
        tvOrderStatus = (TextView) findViewById(R.id.tv_order_status);
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

    public void generateCheckoutCart() {
        Cart cart = PreferenceUtils.getInstance(this).getUserCart();
        final UserCart userCart = new UserCart();
        userCart.setCart(cart);
        userCart.setAccessToken(PreferenceUtils.getInstance(this).getUserProfile().getAccessToken());
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Call<AddedCartResponse> c  = ShoprAPIClient.getApiInterface().sendCartForVerification(userCart);
                c.enqueue(CartActivity.this);
            }
        });
    }

    @Override
    public void onResponse(Call<AddedCartResponse> call, Response<AddedCartResponse> response) {
        if (response.isSuccessful()) {
            setupConfirmationScreen(response.body());
        }
    }

    private void setupConfirmationScreen(AddedCartResponse body) {
        mFragmentManager.popBackStackImmediate();
        flContainer.setVisibility(View.GONE);
        rlContainer.setVisibility(View.VISIBLE);
        Bitmap qrBitmap = QRCode.from(body.getCartId()).bitmap();
        ivCartQr.setImageBitmap(qrBitmap);
        tvCartId.setText(String.format("%s%s", "ORDER ID: ", body.getCartId()));
        tvTimeStamp.setText(Utils.getDate(body.getTimestamp()));
        switch (body.getStatus()){
            case "Active":
            case "active":
                tvOrderStatus.setText("AWAITING VERIFICATION");
                tvOrderStatus.setBackgroundResource(R.drawable.bg_awaiting_response);
                PreferenceUtils.getInstance(this).saveAwaitingCart(body);
                break;
            case "verified":
            case "Verified":
                tvOrderStatus.setText("ORDER VERIFIED");
                tvOrderStatus.setBackgroundResource(R.drawable.bg_verified);
                PreferenceUtils.getInstance(this).saveAwaitingCart(null);
                break;
        }

    }

    @Override
    public void onFailure(Call<AddedCartResponse> call, Throwable t) {

    }

    public void showConfirmationFragment() {
        mFragmentManager.beginTransaction()
                .add(R.id.frame_container, new ConfirmationFragment(), ConfirmationFragment.class.getSimpleName())
                .addToBackStack(ConfirmationFragment.class.getSimpleName())
                .commit();
    }
}
