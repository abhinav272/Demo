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
import com.android.shopr.adapters.StoresRecyclerViewAdapter;
import com.android.shopr.api.ShoprAPIClient;
import com.android.shopr.model.Store;
import com.android.shopr.utils.ExecutorSupplier;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by abhinav.sharma on 1/8/2017.
 */

public class HomeFragment extends BaseFragment implements Callback<Store.List>,StoresRecyclerViewAdapter.DelegateEvent {

    private static final String TAG = "HomeFragment";
    private RecyclerView mRecyclerView;
    private StoresRecyclerViewAdapter mStoresRecyclerViewAdapter;
    private Store.List mStores;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(getLayoutManager());
        getAllStores();
    }

    private void getAllStores() {
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String emptyBody = "";
                Call<Store.List> call = ShoprAPIClient.getApiInterface().getAllStores(emptyBody);
                call.enqueue(HomeFragment.this);
            }
        });
    }

    private GridLayoutManager getLayoutManager(){
        return new GridLayoutManager(getActivity(), 2);
    }

    @Override
    public void onResponse(Call<Store.List> call, Response<Store.List> response) {
        if(response.isSuccessful() && response.code() == 200){
            mStores = response.body();
            mStoresRecyclerViewAdapter = new StoresRecyclerViewAdapter(mStores, getActivity(), this);
            mRecyclerView.setAdapter(mStoresRecyclerViewAdapter);
//            for (Store s:mStores) {
//                Log.e("onResponse: ", s.getStoreName());
//            }
        }
    }

    @Override
    public void onFailure(Call<Store.List> call, Throwable t) {
        Log.e(TAG, "onFailure: ", t);
    }

    @Override
    public void delegateToHost(int position) {
        ((HomeActivity) getActivity()).showCategoriesFragment(position);
    }
}
