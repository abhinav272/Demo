package com.android.shopr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.shopr.HomeActivity;
import com.android.shopr.R;
import com.android.shopr.adapters.ProductsRecyclerViewAdapter;
import com.android.shopr.adapters.animators.SlideInUpAnimator;
import com.android.shopr.model.CategoryWiseProducts;
import com.android.shopr.model.PlaceWiseStores;
import com.android.shopr.model.Product;
import com.android.shopr.model.StoreWiseCategories;
import com.android.shopr.utils.ShoprConstants;

import java.util.List;

import retrofit2.Callback;

/**
 * Created by abhinav.sharma on 21/01/17.
 */

public class ProductsFragment extends BaseFragment implements ProductsRecyclerViewAdapter.DelegateEvent {

    private static final String TAG = "ProductsFragment";
    private RecyclerView mRecyclerView;
    private int categoryId = -1, storeId = -1;
    private StoreWiseCategories mStoreWiseCategories;
    private ProductsRecyclerViewAdapter mProductsRecyclerViewAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(getLayoutManager());
//        mRecyclerView.setItemAnimator(getItemAnimator());
        getAllStores();
    }

    private RecyclerView.ItemAnimator getItemAnimator() {
        return new SlideInUpAnimator();
    }

    private void getAllStores() {
        mStoreWiseCategories = getArguments().getParcelable(ShoprConstants.STORE_CATEGORY_WISE_PRODUCTS);
        setupUI(mStoreWiseCategories);
    }

    private void setupUI(StoreWiseCategories mStoreWiseCategories) {
        mProductsRecyclerViewAdapter = new ProductsRecyclerViewAdapter(getActivity(), mStoreWiseCategories, this);
        mRecyclerView.setAdapter(mProductsRecyclerViewAdapter);
    }

    private GridLayoutManager getLayoutManager(){
        return new GridLayoutManager(getActivity(), 2);
    }

    @Override
    public void delegateToHost(int categoryId, Product product) {

    }
}
