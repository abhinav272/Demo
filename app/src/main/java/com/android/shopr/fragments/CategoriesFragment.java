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
import com.android.shopr.adapters.CategoriesRecyclerViewAdapter;
import com.android.shopr.api.ShoprAPIClient;
import com.android.shopr.model.StoreWiseCategory;
import com.android.shopr.utils.ExecutorSupplier;
import com.android.shopr.utils.ShoprConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by abhinav.sharma on 21/01/17.
 */
public class CategoriesFragment extends BaseFragment implements Callback<StoreWiseCategory>, CategoriesRecyclerViewAdapter.DelegateEvent {

    private static final String TAG = "CategoriesFragment";
    private RecyclerView mRecyclerView;
    private StoreWiseCategory mStoreWiseCategory;
    private CategoriesRecyclerViewAdapter mCategoriesRecyclerViewAdapter;
    private int storeId = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        storeId = b.getInt(ShoprConstants.STORE_ID);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(getLayoutManager());

        getStoreWiseCategories(storeId);
    }

    private void getStoreWiseCategories(final int storeId) {
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ShoprAPIClient.getApiInterface().getStoreWiseCategories(storeId).enqueue(CategoriesFragment.this);
            }
        });
    }

    private GridLayoutManager getLayoutManager() {
        return new GridLayoutManager(getActivity(), 2);
    }


    @Override
    public void onResponse(Call<StoreWiseCategory> call, Response<StoreWiseCategory> response) {
        if (response.isSuccessful() && response.code() == 200) {
            mStoreWiseCategory = response.body();
            mCategoriesRecyclerViewAdapter = new CategoriesRecyclerViewAdapter(getActivity(), this, mStoreWiseCategory);
            mRecyclerView.setAdapter(mCategoriesRecyclerViewAdapter);
            ((HomeActivity) getActivity()).pushTitleStack(mStoreWiseCategory.getStoreName());
        }
    }

    @Override
    public void onFailure(Call<StoreWiseCategory> call, Throwable t) {
        Log.e(TAG, "onFailure: ", t);
    }

    @Override
    public void delegateToHost(int storeId, int categoryId) {
        ((HomeActivity) getActivity()).showProductsFragment(storeId, categoryId);
    }
}
