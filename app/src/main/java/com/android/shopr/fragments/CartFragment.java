package com.android.shopr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shopr.CartActivity;
import com.android.shopr.R;
import com.android.shopr.adapters.CartRecyclerViewAdapter;
import com.android.shopr.model.Cart;

/**
 * Created by Abhinav on 02/04/17.
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {

    private ImageView ivBack;
    private Cart cart;
    private RecyclerView recyclerView;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;
    private TextView tvStoreNameAndLocation, tvTotalItems, tvCartTotal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cart = ((CartActivity) getActivity()).getCart();
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        tvStoreNameAndLocation = (TextView) view.findViewById(R.id.tv_store_and_location);
        tvTotalItems = (TextView) view.findViewById(R.id.tv_items_placeholder);
        tvCartTotal = (TextView) view.findViewById(R.id.tv_cart_total);
        ivBack.setOnClickListener(this);
        setupCart();
    }

    private void setupCart() {
        tvStoreNameAndLocation.setText(cart.getStoreNameAndAddress());
        tvCartTotal.setText("INR " + cart.getCartTotal());
        tvTotalItems.setText("ITEMS(" + cart.getCartItems().size() + ")");
        recyclerView.setLayoutManager(getLayoutManager());
        cartRecyclerViewAdapter = new CartRecyclerViewAdapter(getActivity(), cart);
        recyclerView.setAdapter(cartRecyclerViewAdapter);
    }

    private LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    public void onClick(View v) {
        getActivity().finish();
    }
}
