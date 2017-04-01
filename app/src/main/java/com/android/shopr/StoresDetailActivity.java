package com.android.shopr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shopr.adapters.StoresDetailAdapter;
import com.android.shopr.model.PlaceWiseStores;
import com.android.shopr.utils.ShoprConstants;

/**
 * Created by Abhinav on 30/03/17.
 */
public class StoresDetailActivity extends BaseActivity implements View.OnClickListener {

    private PlaceWiseStores placeWiseStores;
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private ImageView ivBack;
    private Toolbar mToolBar;
    private TextView tvStoreName, tvLocationName;
    private String locationName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        placeWiseStores = getIntent().getParcelableExtra(ShoprConstants.STORE_POJO);
        locationName = getIntent().getStringExtra(ShoprConstants.STORE_LOCATION);
        initView();
        setupCategoriesAndProducts(placeWiseStores);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tl_tab);
        mToolBar = ((Toolbar) findViewById(R.id.toolbar));
        ivBack = (ImageView) mToolBar.findViewById(R.id.iv_back);
        tvStoreName = (TextView) mToolBar.findViewById(R.id.tv_store_name);
        tvLocationName = (TextView) mToolBar.findViewById(R.id.tv_location_name);
        ivBack.setOnClickListener(this);
        mTabLayout.setupWithViewPager(viewPager);
    }

    private void setupCategoriesAndProducts(PlaceWiseStores placeWiseStores) {
        if (placeWiseStores.getCategories().size() < 4) {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
        StoresDetailAdapter adapter = new StoresDetailAdapter(getSupportFragmentManager(), placeWiseStores);
        viewPager.setAdapter(adapter);

        tvStoreName.setText(placeWiseStores.getStoreName());
        tvLocationName.setText(locationName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
