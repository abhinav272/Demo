package com.android.shopr.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.shopr.fragments.HomeFragment;
import com.android.shopr.model.PlaceWiseCategories;
import com.android.shopr.model.PlaceWiseCategoriesStores;
import com.android.shopr.model.PlaceWiseStores;
import com.android.shopr.utils.ShoprConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhinav on 09/03/17.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private PlaceWiseCategoriesStores mPlaceWiseCategoriesStores;

    public ViewPagerAdapter(FragmentManager fm, PlaceWiseCategoriesStores mPlaceWiseCategoriesStores) {
        super(fm);
        this.mPlaceWiseCategoriesStores = mPlaceWiseCategoriesStores;
        List<PlaceWiseCategories> placeWiseCategories = mPlaceWiseCategoriesStores.getCategories();
        placeWiseCategories.add(0,null);
        mPlaceWiseCategoriesStores.setCategories(placeWiseCategories);
    }

    @Override
    public Fragment getItem(int position) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        ArrayList<PlaceWiseStores> stores = new ArrayList<>();
        if (position==0){
            bundle.putParcelableArrayList(ShoprConstants.PLACE_WISE_STORE_LIST, new ArrayList<Parcelable>(mPlaceWiseCategoriesStores.getStores()));
            homeFragment.setArguments(bundle);
            return homeFragment;
        }
        List<Integer> storeIds = mPlaceWiseCategoriesStores.getCategories().get(position).getStoresList();
        for (PlaceWiseStores pStores : mPlaceWiseCategoriesStores.getStores()) {
            if (storeIds.contains(pStores.getStoreId()))
                stores.add(pStores);
        }
        bundle.putParcelableArrayList(ShoprConstants.PLACE_WISE_STORE_LIST, stores);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public int getCount() {
        return mPlaceWiseCategoriesStores.getCategories().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0)
            return "All";
        return mPlaceWiseCategoriesStores.getCategories().get(position).getCategoryName();
    }
}
