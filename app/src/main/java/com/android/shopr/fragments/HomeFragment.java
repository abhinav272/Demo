package com.android.shopr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.android.shopr.HomeActivity;
import com.android.shopr.R;
import com.android.shopr.adapters.StoresRecyclerViewAdapter;
import com.android.shopr.adapters.animators.SlideInUpAnimator;
import com.android.shopr.api.ShoprAPIClient;
import com.android.shopr.model.PlaceWiseStores;
import com.android.shopr.model.Store;
import com.android.shopr.utils.ExecutorSupplier;
import com.android.shopr.utils.ShoprConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by abhinav.sharma on 1/8/2017.
 */

public class HomeFragment extends BaseFragment implements StoresRecyclerViewAdapter.DelegateEvent {

    private static final String TAG = "HomeFragment";
    private RecyclerView mRecyclerView;
    private StoresRecyclerViewAdapter mStoresRecyclerViewAdapter;
    private List<PlaceWiseStores> placeWiseStores;

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
        ((HomeActivity) getActivity()).hideFAB();
        getAllStores();
    }

    private RecyclerView.ItemAnimator getItemAnimator() {
        return new SlideInUpAnimator();
    }

    private void getAllStores() {
        placeWiseStores = getArguments().getParcelableArrayList(ShoprConstants.PLACE_WISE_STORE_LIST);
        setupUI(placeWiseStores);
    }

    private void setupUI(List<PlaceWiseStores> placeWiseStores) {
        mStoresRecyclerViewAdapter = new StoresRecyclerViewAdapter(placeWiseStores, getActivity(), this);
        mRecyclerView.setAdapter(mStoresRecyclerViewAdapter);
    }

    private GridLayoutManager getLayoutManager(){
        return new GridLayoutManager(getActivity(), 2);
    }

    @Override
    public void delegateToHost(int position) {
//        ((HomeActivity) getActivity()).showCategoriesFragment(position);
        ((HomeActivity) getActivity()).showStoresDetailFragment(position);
    }
}
