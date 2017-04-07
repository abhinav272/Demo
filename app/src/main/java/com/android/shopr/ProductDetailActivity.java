package com.android.shopr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.android.shopr.fragments.ProductDetailFragment;
import com.android.shopr.model.Product;
import com.android.shopr.utils.ShoprConstants;

/**
 * Created by Abhinav on 01/04/17.
 */
public class ProductDetailActivity extends BaseActivity {

    private int categoryId, storeId;
    private Product product;
    private FragmentManager mFragmentManager;
    private String storeName, locationName;

    public String getStoreName() {
        return storeName;
    }

    public String getLocationName() {
        return locationName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getStoreId() {
        return storeId;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        categoryId = getIntent().getIntExtra(ShoprConstants.CATEGORY_ID, -1);
        product = getIntent().getParcelableExtra(ShoprConstants.PRODUCT_OBJ);
        storeName = getIntent().getStringExtra(ShoprConstants.STORE_NAME);
        locationName = getIntent().getStringExtra(ShoprConstants.STORE_LOCATION);
        storeId = getIntent().getIntExtra(ShoprConstants.STORE_ID, -1);
        mFragmentManager = getSupportFragmentManager();
        showProductDetailFragment();
    }

    private void showProductDetailFragment() {
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ShoprConstants.PRODUCT_OBJ, product);
        productDetailFragment.setArguments(bundle);
        mFragmentManager.beginTransaction()
                .add(R.id.frame_container, productDetailFragment, ProductDetailFragment.class.getSimpleName())
                .commit();
    }


}
