package com.android.shopr;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.android.shopr.utils.Utils;
import com.google.zxing.Result;
import com.squareup.picasso.Picasso;

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
    private AlertDialog alertDialog;

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
        findViewById(R.id.btn_scan2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flScanProducts.performClick();
            }
        });
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
            showDialogForScannedProduct(productFromBarcode);
        }
    }

    private void showDialogForScannedProduct(ProductFromBarcode productFromBarcode) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_scan_popup, null);
        dialogBuilder.setView(dialogView);
        setupUI(dialogView, productFromBarcode);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void setupUI(View dialogView, final ProductFromBarcode productFromBarcode) {
        ImageView dProductImage = ((ImageView) dialogView.findViewById(R.id.iv_product_image));
        Picasso.with(this).load(productFromBarcode.getProduct().getImageUrl()).centerCrop().fit()
                .placeholder(new ColorDrawable(Utils.getRandomBackgroundColor())).into(dProductImage);
        TextView dProductName = (TextView) dialogView.findViewById(R.id.tv_product_name);
        TextView dProductPriceAfterDiscount = (TextView) dialogView.findViewById(R.id.tv_product_price_after_discount);
        TextView dProductPriceOriginal = (TextView) dialogView.findViewById(R.id.tv_product_price_original);
        TextView dProductDiscount = (TextView) dialogView.findViewById(R.id.tv_product_discount);
        final TextView dProductQuantity = (TextView) dialogView.findViewById(R.id.tv_product_quantity);
        ImageView dIncreaseQuantity = (ImageView) dialogView.findViewById(R.id.iv_increase_qty);
        ImageView dDecreaseQuantity = (ImageView) dialogView.findViewById(R.id.iv_decrease_qty);
        FrameLayout dAddToCart = (FrameLayout) dialogView.findViewById(R.id.fl_scan_and_add_to_cart);
        FrameLayout dAddToWatchlist = (FrameLayout) dialogView.findViewById(R.id.fl_watch_product);

        tvSizeS = (TextView) dialogView.findViewById(R.id.tv_size_s);
        tvSizeM = (TextView) dialogView.findViewById(R.id.tv_size_m);
        tvSizeL = (TextView) dialogView.findViewById(R.id.tv_size_l);
        tvSizeXL = (TextView) dialogView.findViewById(R.id.tv_size_xl);
        tvSizeXXL = (TextView) dialogView.findViewById(R.id.tv_size_xxl);
        tvSizeXXXL = (TextView) dialogView.findViewById(R.id.tv_size_xxxl);

        tvSizeS.setOnClickListener(sizeListener);
        tvSizeM.setOnClickListener(sizeListener);
        tvSizeL.setOnClickListener(sizeListener);
        tvSizeXL.setOnClickListener(sizeListener);
        tvSizeXXL.setOnClickListener(sizeListener);
        tvSizeXXXL.setOnClickListener(sizeListener);
        dAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (size > -1){
                    Utils.addProductToCart(StoresDetailActivity.this, productFromBarcode, size,
                            Integer.parseInt(dProductQuantity.getText().toString()));
                    alertDialog.dismiss();
                } else {
                    showShortToast("Please select Size");
                }

            }
        });
        dAddToWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        dProductQuantity.setText("1");
        dIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quant = Integer.parseInt(dProductQuantity.getText().toString());
                quant += 1;
                dProductQuantity.setText(String.valueOf(quant));
            }
        });
        dDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quant = Integer.parseInt(dProductQuantity.getText().toString());
                if (quant > 1)
                    dProductQuantity.setText(String.valueOf(quant - 1));
            }
        });

        dProductName.setText(productFromBarcode.getProduct().getProductName());
        dProductDiscount.setText(String.format("%s %S", productFromBarcode.getProduct().getDiscount(), "OFF"));
        dProductPriceAfterDiscount.setText(String.format("%s%s", getString(R.string.ruppee_symbol) ,
                productFromBarcode.getProduct().getPriceAfterDiscount()));
        dProductPriceOriginal.setText(String.format("%s%s", getString(R.string.ruppee_symbol) ,
                productFromBarcode.getProduct().getPriceBeforeDiscount()));


    }

    @Override
    public void onFailure(Call<ProductFromBarcode> call, Throwable t) {

    }

    private TextView tvSizeS;
    private TextView tvSizeM;
    private TextView tvSizeL;
    private TextView tvSizeXL;
    private TextView tvSizeXXL;
    private TextView tvSizeXXXL;
    private int size = -1;
    private View.OnClickListener sizeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_size_s:
                    tvSizeS.setBackgroundResource(R.drawable.bg_circular_selected);
                    tvSizeS.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorAccent));
                    tvSizeM.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeM.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    size = 0;
                    break;
                case R.id.tv_size_m:
                    tvSizeS.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeS.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeM.setBackgroundResource(R.drawable.bg_circular_selected);
                    tvSizeM.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorAccent));
                    tvSizeL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    size = 1;
                    break;
                case R.id.tv_size_l:
                    tvSizeS.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeS.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeM.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeM.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeL.setBackgroundResource(R.drawable.bg_circular_selected);
                    tvSizeL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorAccent));
                    tvSizeXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    size = 2;
                    break;
                case R.id.tv_size_xl:
                    tvSizeS.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeS.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeM.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeM.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXL.setBackgroundResource(R.drawable.bg_circular_selected);
                    tvSizeXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorAccent));
                    tvSizeXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    size = 3;
                    break;
                case R.id.tv_size_xxl:
                    tvSizeS.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeS.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeM.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeM.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXXL.setBackgroundResource(R.drawable.bg_circular_selected);
                    tvSizeXXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorAccent));
                    tvSizeXXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    size = 4;
                    break;
                case R.id.tv_size_xxxl:
                    tvSizeS.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeS.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeM.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeM.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXXL.setBackgroundResource(R.drawable.bg_circular);
                    tvSizeXXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorDarkGrey));
                    tvSizeXXXL.setBackgroundResource(R.drawable.bg_circular_selected);
                    tvSizeXXXL.setTextColor(ContextCompat.getColor(StoresDetailActivity.this, R.color.colorAccent));
                    size = 5;
                    break;

            }
        }
    };
}
