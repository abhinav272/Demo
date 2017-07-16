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
import com.android.shopr.adapters.StoresRecyclerViewAdapter;
import com.android.shopr.adapters.animators.SlideInUpAnimator;
import com.android.shopr.model.PlaceWiseStores;
import com.android.shopr.utils.ShoprConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhinav.sharma on 1/8/2017.
 */

public class HomeFragment extends BaseFragment implements StoresRecyclerViewAdapter.DelegateEvent {

    private static final String TAG = "HomeFragment";
    private RecyclerView mRecyclerView;
    private StoresRecyclerViewAdapter mStoresRecyclerViewAdapter;
    private List<PlaceWiseStores> placeWiseStores, offSetList;

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
        getAllStores();
    }

    private void getAllStores() {
        placeWiseStores = getArguments().getParcelableArrayList(ShoprConstants.PLACE_WISE_STORE_LIST);
        offSetList = new ArrayList<>();
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
    public void delegateToHost(int storeId) {
        ((HomeActivity) getActivity()).showStoresDetailActivity(storeId);
    }

    public void searchWithTerm(String s) {
        offSetList.clear();
        if (s.length() == 0){
            setupUI(placeWiseStores);
        } else {
            for (PlaceWiseStores store : placeWiseStores) {
                if (store.getStoreName().toLowerCase().contains(s))
                    offSetList.add(store);
            }
            setupUI(offSetList);
        }
    }
}
