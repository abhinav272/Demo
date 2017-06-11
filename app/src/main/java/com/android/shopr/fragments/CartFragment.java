package com.android.shopr.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.shopr.CartActivity;
import com.android.shopr.R;
import com.android.shopr.adapters.CartRecyclerViewAdapter;
import com.android.shopr.api.ShoprAPIClient;
import com.android.shopr.model.AddedCartResponse;
import com.android.shopr.model.Cart;
import com.android.shopr.model.CartItem;
import com.android.shopr.model.UserCart;
import com.android.shopr.utils.ExecutorSupplier;
import com.android.shopr.utils.PreferenceUtils;
import com.google.gson.Gson;

import net.glxn.qrgen.android.QRCode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Abhinav on 02/04/17.
 */
public class CartFragment extends BaseFragment implements View.OnClickListener, CartRecyclerViewAdapter.ProductListener, Callback<AddedCartResponse> {

    private static final String TAG = "CartFragment";
    private ImageView ivBack, ivCartQr;
    private Cart cart;
    private RecyclerView recyclerView;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;
    private TextView tvStoreNameAndLocation, tvTotalItems, tvCartTotal, tvCheckout, tvQRBanner;
    private RelativeLayout rlContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cart = ((CartActivity) getActivity()).getCart();
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivCartQr = (ImageView) view.findViewById(R.id.iv_cart_qr);
        tvQRBanner = (TextView) view.findViewById(R.id.tv_qr_banner);
        rlContainer = (RelativeLayout) view.findViewById(R.id.rl_container);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        tvStoreNameAndLocation = (TextView) view.findViewById(R.id.tv_store_and_location);
        tvTotalItems = (TextView) view.findViewById(R.id.tv_items_placeholder);
        tvCartTotal = (TextView) view.findViewById(R.id.tv_cart_total);
        tvCheckout = (TextView) view.findViewById(R.id.tv_checkout);
        tvCheckout.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        setupCart();
        setupRecyclerView();
    }

    private void setupCart() {
        tvStoreNameAndLocation.setText(cart.getStoreNameAndAddress());
        tvCartTotal.setText("INR " + String.format("%.2f",cart.getCartTotal()));
        tvTotalItems.setText("ITEMS(" + getTotalItems() + ")");
    }

    private String getTotalItems() {
        int items = 0;
        for (CartItem item : cart.getCartItems()) {
            items+= item.getProductQuantity();
        }
        return String.valueOf(items);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(getLayoutManager());
        cartRecyclerViewAdapter = new CartRecyclerViewAdapter(getActivity(), cart);
        recyclerView.setAdapter(cartRecyclerViewAdapter);
        cartRecyclerViewAdapter.setProductListener(this);
    }

    private LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_checkout:
                generateCheckoutCart();
                break;
            case R.id.iv_back:
                getActivity().finish();
                break;
        }
    }

    private void generateCheckoutCart() {
        Cart cart = PreferenceUtils.getInstance(getActivity()).getUserCart();
        final UserCart userCart = new UserCart();
        userCart.setCart(cart);
        userCart.setAccessToken(PreferenceUtils.getInstance(getActivity()).getUserProfile().getAccessToken());
//        String cartJson = new Gson().toJson(cart);
//        Log.e("generateCheckoutCart: ", cartJson);
//        Bitmap qrBitmap = QRCode.from(cartJson).bitmap();
//        rlContainer.setVisibility(View.INVISIBLE);
//        ivCartQr.setVisibility(View.VISIBLE);
//        ivCartQr.setImageBitmap(qrBitmap);
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Call<AddedCartResponse> c  = ShoprAPIClient.getApiInterface().sendCartForVerification(userCart);
                c.enqueue(CartFragment.this);
            }
        });
    }

    @Override
    public void removeThisProduct(CartItem cartItem) {
        cart.removeCartItem(cartItem);
        PreferenceUtils.getInstance(getActivity()).saveUserCart(cart);
        setupCart();
        cartRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateCartOnUI() {
        setupCart();
    }

    @Override
    public void onResponse(Call<AddedCartResponse> call, Response<AddedCartResponse> response) {
        if (response.isSuccessful()) {
            Bitmap qrBitmap = QRCode.from(response.body().getCartId()).bitmap();
            rlContainer.setVisibility(View.INVISIBLE);
            ivCartQr.setVisibility(View.VISIBLE);
            tvQRBanner.setVisibility(View.VISIBLE);
            ivCartQr.setImageBitmap(qrBitmap);
        }
    }

    @Override
    public void onFailure(Call<AddedCartResponse> call, Throwable t) {
        Log.e(TAG, "onFailure: ", t);
    }
}
