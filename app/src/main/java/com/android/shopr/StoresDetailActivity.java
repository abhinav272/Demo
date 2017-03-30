package com.android.shopr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.android.shopr.adapters.StoresDetailAdapter;
import com.android.shopr.model.PlaceWiseStores;
import com.android.shopr.utils.ShoprConstants;

/**
 * Created by Abhinav on 30/03/17.
 */
public class StoresDetailActivity extends BaseActivity {

    private PlaceWiseStores placeWiseStores;
    private ViewPager viewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        placeWiseStores = getIntent().getParcelableExtra(ShoprConstants.STORE_POJO);
        initView();
        setupCategoriesAndProducts(placeWiseStores);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tl_tab);
        mTabLayout.setupWithViewPager(viewPager);
    }

    private void setupCategoriesAndProducts(PlaceWiseStores placeWiseStores) {
        StoresDetailAdapter adapter = new StoresDetailAdapter(getSupportFragmentManager(), placeWiseStores);
        viewPager.setAdapter(adapter);
    }
}
