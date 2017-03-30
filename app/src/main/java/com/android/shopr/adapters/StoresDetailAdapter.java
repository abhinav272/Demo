package com.android.shopr.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.shopr.fragments.ProductsFragment;
import com.android.shopr.model.CategoryWiseProducts;
import com.android.shopr.model.PlaceWiseStores;
import com.android.shopr.model.Product;
import com.android.shopr.model.StoreWiseCategories;
import com.android.shopr.utils.ShoprConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhinav on 30/03/17.
 */
public class StoresDetailAdapter extends FragmentPagerAdapter {

    private PlaceWiseStores placeWiseStores;
    private List<StoreWiseCategories> storeWiseCategories;

    public StoresDetailAdapter(FragmentManager fm, PlaceWiseStores placeWiseStores) {
        super(fm);
        this.placeWiseStores = placeWiseStores;
        storeWiseCategories = placeWiseStores.getCategories();
//        storeWiseCategories.add(0,null);
    }

    @Override
    public Fragment getItem(int position) {
        ProductsFragment productsFragment = new ProductsFragment();
        Bundle bundle = new Bundle();
//        if (position==0){
//            bundle.putParcelableArrayList(ShoprConstants.STORE_CATEGORY_WISE_PRODUCTS, getAllProducts());
//            productsFragment.setArguments(bundle);
//            return productsFragment;
//        }

        bundle.putParcelable(ShoprConstants.STORE_CATEGORY_WISE_PRODUCTS, getProductsByCategory(storeWiseCategories.get(position).getCategoryId()));
        productsFragment.setArguments(bundle);
        return productsFragment;
    }

    @Override
    public int getCount() {
        return placeWiseStores.getCategories().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        if (position==0)
//            return "All";
        return storeWiseCategories.get(position).getCategoryName();
    }

//    private ArrayList<StoreWiseCategories> getAllProducts() {
//        ArrayList<StoreWiseCategories> allCategoryProducts = new ArrayList<>(storeWiseCategories);
//        allCategoryProducts.remove(0);
//        return allCategoryProducts;
//    }

    private StoreWiseCategories getProductsByCategory(int categoryId) {
        for (StoreWiseCategories category : storeWiseCategories) {
            if (category.getCategoryId() == categoryId)
                return category;
        }
        return null;
    }
}
