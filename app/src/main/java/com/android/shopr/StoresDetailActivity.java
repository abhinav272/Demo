package com.android.shopr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shopr.adapters.StoresDetailAdapter;
import com.android.shopr.model.PlaceWiseStores;
import com.android.shopr.model.Product;
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

    public String getTvStoreName() {
        return tvStoreName.getText().toString();
    }

    public String getTvLocationName() {
        return tvLocationName.getText().toString();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        placeWiseStores = getIntent().getParcelableExtra(ShoprConstants.STORE_POJO);
        locationName = getIntent().getStringExtra(ShoprConstants.STORE_LOCATION);
        initView();
        setupCategoriesAndProducts(placeWiseStores);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stores_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_watch_list:
                // TODO: 09/03/17 Add Search activity or similar feature
                break;
            case R.id.action_cart:
                showCartActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCartActivity() {
        Intent intent = new Intent(StoresDetailActivity.this, CartActivity.class);
        startActivity(intent);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tl_tab);
        mToolBar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(mToolBar);
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

    public void onProductSelected(int categoryId, Product product) {
        showProductDetailActivity(categoryId, product);
    }

    private void showProductDetailActivity(int categoryId, Product product) {
        Intent intent = new Intent(StoresDetailActivity.this, ProductDetailActivity.class);
        intent.putExtra(ShoprConstants.STORE_LOCATION, getTvLocationName());
        intent.putExtra(ShoprConstants.STORE_NAME, getTvStoreName());
        intent.putExtra(ShoprConstants.CATEGORY_ID, categoryId);
        intent.putExtra(ShoprConstants.STORE_ID, placeWiseStores.getStoreId());
        intent.putExtra(ShoprConstants.PRODUCT_OBJ, product);
        startActivity(intent);
    }
}
