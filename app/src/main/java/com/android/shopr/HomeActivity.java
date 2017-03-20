package com.android.shopr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shopr.adapters.ViewPagerAdapter;
import com.android.shopr.fragments.CategoriesFragment;
import com.android.shopr.fragments.HomeFragment;
import com.android.shopr.fragments.ProductDetailFragment;
import com.android.shopr.fragments.ProductsFragment;
import com.android.shopr.fragments.QRFragment;
import com.android.shopr.model.Product;
import com.android.shopr.model.UserProfile;
import com.android.shopr.utils.ExecutorSupplier;
import com.android.shopr.utils.PreferenceUtils;
import com.android.shopr.utils.ShoprConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.zxing.Result;
import com.squareup.picasso.Picasso;

import java.util.Stack;

public class HomeActivity extends BaseActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "HomeActivity";
    private static final int LOCATION_PERMISSION_REQ_CODE = 72;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View mHeaderView;
    private FragmentManager mFragmentManager;
    private Stack<String> mTitleStack;
    private FloatingActionButton floatingActionButton;
    private Result qrResult;
    private static final int CAM_PERMISSION_REQ_CODE = 27;
    public static final int PLACE_PICKER_REQUEST = 1;
    private BottomNavigationView mBottomNavigationView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TextView mPageType, mLocationName;
    private GoogleApiClient mGoogleApiClient;
    private ImageView mManuallySelectPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpViews();
        connectToGoogleClient();
        getPlaces();
//        setUpHomeFragment();
    }

    private void connectToGoogleClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    private void getPlaces() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i(TAG, String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(), placeLikelihood.getLikelihood()));
                }
                if (likelyPlaces.getStatus().isSuccess()) {
                    setLocationName(likelyPlaces.get(0).getPlace().getName() + "");
                    fetchCategoriesAndStores(likelyPlaces.get(0).getPlace());
                }
                likelyPlaces.release();
            }
        });
    }

    private void fetchCategoriesAndStores(Place place) {
        // TODO: 19/03/17 API call for categories and stores
        Log.d(TAG, "fetchCategoriesAndStores: " + place.getName());
    }

    private void setUpNavigationItems(UserProfile userProfile) {

        ImageView iv = (ImageView) mHeaderView.findViewById(R.id.iv_user_image);
        Picasso.with(this).load(userProfile.getPicUrl()).fit().centerCrop().into(iv);
        Log.e(TAG, "setUpNavigationItems: " + userProfile.getPicUrl());
        ((TextView) mHeaderView.findViewById(R.id.tv_user_name)).setText(userProfile.getPersonName());

    }

    private void setUpViews() {
        mTitleStack = new Stack<>();
        mFragmentManager = getSupportFragmentManager();
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        navigationView = (NavigationView) findViewById(R.id.nvView);
        mTitle = getTitle();
        mTitleStack.push((String) mTitle);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setActionBarTitle();
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setActionBarTitle();
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_shopr_drawer);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mHeaderView = getHeaderView(navigationView);
        floatingActionButton.setOnClickListener(this);
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final UserProfile userProfile = PreferenceUtils.getInstance(HomeActivity.this).getUserProfile();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setUpNavigationItems(userProfile);
                    }
                });
            }
        });
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
//        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mTabLayout = (TabLayout) findViewById(R.id.tl_tab);
        mViewPager = ((ViewPager) findViewById(R.id.viewpager));
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        mPageType = (TextView) toolbar.findViewById(R.id.tv_page_type);
        mLocationName = (TextView) toolbar.findViewById(R.id.tv_location_name);
        toolbar.findViewById(R.id.ll_location).setOnClickListener(this);
    }

    public void setPageType(String pageType) {
        mPageType.setText(pageType);
    }

    public void setLocationName(String locationName) {
        mLocationName.setText(locationName);
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment(), "ONE");
        adapter.addFragment(new Fragment(), "TWO");
        adapter.addFragment(new Fragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // TODO: 09/03/17 Add Search activity or similar feature
                break;
            case R.id.action_cart:
                showShortToast("Cart");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    private View getHeaderView(NavigationView navigationView) {
        if (navigationView.getHeaderCount() > 0)
            return navigationView.getHeaderView(0);
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public void setUpHomeFragment() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, new HomeFragment(), HomeFragment.class.getSimpleName());
//        fragmentTransaction.addToBackStack(HomeFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }


    public void showCategoriesFragment(int position) {
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        Bundle argBundle = new Bundle();
        argBundle.putInt(ShoprConstants.STORE_ID, position);
        categoriesFragment.setArguments(argBundle);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_container, categoriesFragment, CategoriesFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(CategoriesFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    public void showProductsFragment(int storeId, int categoryId) {
        ProductsFragment productsFragment = new ProductsFragment();
        Bundle argBundle = new Bundle();
        argBundle.putInt(ShoprConstants.STORE_ID, storeId);
        argBundle.putInt(ShoprConstants.CATEGORY_ID, categoryId);
        productsFragment.setArguments(argBundle);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_container, productsFragment, ProductsFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(ProductsFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    public void showProductDetailFragment(int storeId, int categoryId, Product product) {
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        Bundle argBundle = new Bundle();
        argBundle.putInt(ShoprConstants.STORE_ID, storeId);
        argBundle.putInt(ShoprConstants.CATEGORY_ID, categoryId);
        argBundle.putParcelable(ShoprConstants.PRODUCT_OBJ, product);
        productDetailFragment.setArguments(argBundle);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_container, productDetailFragment, ProductDetailFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(ProductDetailFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    private void setActionBarTitle() {
//        mTitle = title;
        if (!mTitleStack.empty())
            getSupportActionBar().setTitle(mTitleStack.peek());
    }

    public void popTitleStack() {
        if (!mTitleStack.empty()) {
            mTitleStack.pop();
            setActionBarTitle();
        }
    }

    public void pushTitleStack(String title) {
        mTitleStack.push(title);
        setActionBarTitle();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        popTitleStack();
        if (mTitleStack != null && !mTitleStack.empty() && mTitleStack.peek().equalsIgnoreCase(getString(R.string.app_name))) {
            hideFAB();
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                showQRFragment();
                break;
            case R.id.ll_location:
                manuallySelectLocation();
                break;
        }
    }

    private void manuallySelectLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();

        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                setLocationName(place.getName()+"");
                fetchCategoriesAndStores(place);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAM_PERMISSION_REQ_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showQRFragment();
        } else if(requestCode == LOCATION_PERMISSION_REQ_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getPlaces();
        }
    }

    private void showQRFragment() {
        requestCameraPermission();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_container, new QRFragment(), QRFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(QRFragment.class.getSimpleName());
        fragmentTransaction.commit();

    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAM_PERMISSION_REQ_CODE);
            }
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQ_CODE);
        }
    }

    public void setQRResult(Result result) {
        if (result != null) {
            qrResult = result;
            popTitleStack();
            showResult(result);
        }
    }

    private void showResult(Result result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(result.getBarcodeFormat().toString());
        builder.setMessage(result.getText());
        builder.create().show();

    }

    public void hideFAB() {
        floatingActionButton.hide();
    }

    public void showFAB() {
        floatingActionButton.show();
    }

    public void hideBNV() {
        mBottomNavigationView.setVisibility(View.GONE);
    }

    public void showBNV() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_quick_checkout:
                setPageType(getString(R.string.text_quick_checkout));
                break;
            case R.id.action_sales:
                setPageType(getString(R.string.text_sale));
                break;
            case R.id.action_shops:
                setPageType(getString(R.string.text_shops));
                break;
        }

        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage() + " code : " + connectionResult.getErrorCode());
    }
}
