package com.android.shopr;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.shopr.adapters.StoresDetailAdapter;
import com.android.shopr.api.ShoprAPIClient;
import com.android.shopr.fragments.QRFragment;
import com.android.shopr.model.PlaceWiseStores;
import com.android.shopr.model.Product;
import com.android.shopr.model.ProductFromBarcode;
import com.android.shopr.utils.ExecutorSupplier;
import com.android.shopr.utils.ShoprConstants;
import com.google.zxing.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Abhinav on 30/03/17.
 */
public class StoresDetailActivity extends BaseActivity implements View.OnClickListener, Callback<ProductFromBarcode> {

    private PlaceWiseStores placeWiseStores;
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private ImageView ivBack;
    private Toolbar mToolBar;
    private TextView tvStoreName, tvLocationName;
    private String locationName;
    private FrameLayout flScanProducts;
    private RelativeLayout rlContainer;
    private static final int CAM_PERMISSION_REQ_CODE = 27;
    private FragmentManager mFragmentManager;
    private Result qrResult;

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
        mFragmentManager = getSupportFragmentManager();
        rlContainer = (RelativeLayout) findViewById(R.id.rl_container);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tl_tab);
        mToolBar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(mToolBar);
        ivBack = (ImageView) mToolBar.findViewById(R.id.iv_back);
        tvStoreName = (TextView) mToolBar.findViewById(R.id.tv_store_name);
        tvLocationName = (TextView) mToolBar.findViewById(R.id.tv_location_name);
        flScanProducts = (FrameLayout) findViewById(R.id.btn_scan);
        flScanProducts.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_scan:
                showQRFragment();
                break;
        }
    }

    private void showQRFragment() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
            return;
        }
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.rl_container, new QRFragment(), QRFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(QRFragment.class.getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();

    }

    public void setQRResult(Result result) {
        if (result != null) {
            qrResult = result;
            getProductByBarcode(result);
        }
    }

    private void getProductByBarcode(final Result result) {
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Call<ProductFromBarcode> call = ShoprAPIClient.getApiInterface().getProductFromBarcode(result.getText());
                call.enqueue(StoresDetailActivity.this);
            }
        });
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

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAM_PERMISSION_REQ_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAM_PERMISSION_REQ_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showQRFragment();
        }
    }

    @Override
    public void onResponse(Call<ProductFromBarcode> call, Response<ProductFromBarcode> response) {
        if (response.isSuccessful() && response.body() != null) {
            ProductFromBarcode productFromBarcode = response.body();
            tvStoreName.setText(productFromBarcode.getStoreName());
            showProductDetailActivity(productFromBarcode.getCategoryId(), productFromBarcode.getProduct());
        }
    }

    @Override
    public void onFailure(Call<ProductFromBarcode> call, Throwable t) {

    }
}
