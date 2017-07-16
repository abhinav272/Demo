package com.android.shopr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.shopr.CartActivity;
import com.android.shopr.R;
import com.android.shopr.adapters.CartBaseAdapter;
import com.android.shopr.model.Cart;

/**
 * Created by Abhinav on 29/06/17.
 */
public class ConfirmationFragment extends BaseFragment implements View.OnClickListener {

    private Cart cart;
    private ListView listView;
    private TextView tvStoreNameAndLocation;
    private TextView tvTotalItems;
    private TextView tvCartTotal;
    private TextView tvTax;
    private double taxPercent = 0.10;
    private TextView tvSubTotal;
    private TextView tvProceedToPayment;
    private CartBaseAdapter cartBaseAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cart = ((CartActivity) getActivity()).getCart();
        return inflater.inflate(R.layout.fragment_cart_confirmation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.lv_items);
        tvStoreNameAndLocation = (TextView) view.findViewById(R.id.tv_store_and_location);
        tvTotalItems = (TextView) view.findViewById(R.id.tv_items_placeholder);
        tvCartTotal = (TextView) view.findViewById(R.id.tv_total_payable);
        tvTax = (TextView) view.findViewById(R.id.tv_tax);
        tvSubTotal = (TextView) view.findViewById(R.id.tv_sub_total);
        listView = (ListView) view.findViewById(R.id.lv_items);
        tvProceedToPayment = (TextView) view.findViewById(R.id.tv_proceed_to_payment);
        tvProceedToPayment.setOnClickListener(this);

        setupCart();
        setupListView();
    }

    private void setupListView() {
        cartBaseAdapter = new CartBaseAdapter(getActivity(), cart);
        listView.setAdapter(cartBaseAdapter);
    }

    private void setupCart() {
        tvStoreNameAndLocation.setText(cart.getStoreNameAndAddress());
        tvTax.setText("INR " + String.format("%.2f", (cart.getCartTotal() * taxPercent)));
        tvSubTotal.setText("INR " + String.format("%.2f", cart.getCartTotal()));
        tvCartTotal.setText("INR " + String.format("%.2f",(cart.getCartTotal() * 1.10)));
        tvTotalItems.setText("ITEMS(" + cart.getTotalItems() + ")");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_proceed_to_payment:
                ((CartActivity) getActivity()).generateCheckoutCart();
                break;
        }
    }
}
