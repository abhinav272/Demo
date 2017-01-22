package com.android.shopr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.shopr.HomeActivity;
import com.android.shopr.R;
import com.android.shopr.adapters.ProductsRecyclerViewAdapter;
import com.android.shopr.api.ShoprAPIClient;
import com.android.shopr.model.CategoryWiseProducts;
import com.android.shopr.utils.ExecutorSupplier;
import com.android.shopr.utils.ShoprConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by abhinav.sharma on 21/01/17.
 */

public class ProductsFragment extends BaseFragment implements Callback<CategoryWiseProducts>,ProductsRecyclerViewAdapter.DelegateEvent {

    private static final String TAG = "ProductsFragment";
    private RecyclerView mRecyclerView;
    private int categoryId = -1, storeId = -1;
    private CategoryWiseProducts mCategoryWiseProducts;
    private ProductsRecyclerViewAdapter mProductsRecyclerViewAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        storeId = bundle.getInt(ShoprConstants.STORE_ID, -1);
        categoryId = bundle.getInt(ShoprConstants.CATEGORY_ID, -1);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(getLayoutManager());
        getCategoryWiseProducts(storeId, categoryId);
    }

    private void getCategoryWiseProducts(final int storeId, final int categoryId) {
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ShoprAPIClient.getApiInterface().getCategorySpecificProducts(storeId, categoryId).enqueue(ProductsFragment.this);
            }
        });
    }

    private GridLayoutManager getLayoutManager() {
        return new GridLayoutManager(getActivity(), 2);
    }

    @Override
    public void onResponse(Call<CategoryWiseProducts> call, Response<CategoryWiseProducts> response) {
        if (response.isSuccessful() && response.code() == 200) {
            mCategoryWiseProducts = response.body();
            mProductsRecyclerViewAdapter = new ProductsRecyclerViewAdapter(getActivity(), this, mCategoryWiseProducts);
            mRecyclerView.setAdapter(mProductsRecyclerViewAdapter);
            ((HomeActivity) getActivity()).setActionBarTitle(mCategoryWiseProducts.getCategoryName());
        }
    }

    @Override
    public void onFailure(Call<CategoryWiseProducts> call, Throwable t) {
        Log.e(TAG, "onFailure: ", t);
    }

    @Override
    public void delegateToHost(int storeId, int categoryId, int productId) {
        Log.d(TAG, "delegateToHost: " + storeId + " " + categoryId);
    }
}
