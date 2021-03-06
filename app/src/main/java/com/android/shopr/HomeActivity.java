package com.android.shopr;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.shopr.adapters.ViewPagerAdapter;
import com.android.shopr.api.ShoprAPIClient;
import com.android.shopr.fragments.CartFragment;
import com.android.shopr.fragments.CategoriesFragment;
import com.android.shopr.fragments.HomeFragment;
import com.android.shopr.fragments.ProductDetailFragment;
import com.android.shopr.fragments.ProductsFragment;
import com.android.shopr.fragments.QRFragment;
import com.android.shopr.model.Cart;
import com.android.shopr.model.LikelyPlaces;
import com.android.shopr.model.PlaceWiseCategories;
import com.android.shopr.model.PlaceWiseCategoriesStores;
import com.android.shopr.model.Product;
import com.android.shopr.model.Store;
import com.android.shopr.model.UserProfile;
import com.android.shopr.utils.ExecutorSupplier;
import com.android.shopr.utils.PreferenceUtils;
import com.android.shopr.utils.ShoprConstants;
import com.android.shopr.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.zxing.Result;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, Callback<PlaceWiseCategoriesStores>, TextWatcher {

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
    public static final int PLACE_PICKER_REQUEST = 1;
    private BottomNavigationView mBottomNavigationView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TextView mPageType, mLocationName;
    private GoogleApiClient mGoogleApiClient;
    private ImageView mManuallySelectPlace;
    private PlaceWiseCategoriesStores placeWiseCategoriesStores;
    private EditText edSearchStore;
    private RelativeLayout rlContainer;
    private InputMethodManager inputMethodManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        setUpViews();
        connectToGoogleClient();
        getPlaces();
//        setUpHomeFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        supportInvalidateOptionsMenu();
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
        Log.d(TAG, "fetchCategoriesAndStores: " + place.getName());
        fetchCategoriesAndStores(place.getId());
    }

    private void fetchCategoriesAndStores(final String placeId) {
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Call<PlaceWiseCategoriesStores> call = ShoprAPIClient.getApiInterface().getPlaceWiseCategoriesStores(placeId);
                call.enqueue(HomeActivity.this);
            }
        });
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
        edSearchStore = (EditText) toolbar.findViewById(R.id.ed_search_store);
        edSearchStore.addTextChangedListener(this);
        edSearchStore.setVisibility(View.INVISIBLE);
        rlContainer = (RelativeLayout) toolbar.findViewById(R.id.rl_container);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mHeaderView = getHeaderView(navigationView);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                showSearchBarWithAnimation();
                break;
            case R.id.action_cart:
                showCartActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSearchBarWithAnimation() {
        View myView = edSearchStore;
        int cx = myView.getWidth();
        int cy = myView.getHeight() / 2;

        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, cx);
        }

        myView.setVisibility(View.VISIBLE);
        rlContainer.setVisibility(View.GONE);
        if (anim != null) {
            anim.start();
        }
        edSearchStore.requestFocus();

        Utils.showKeyboard(this);
    }

    private void hideSearchBarWithAnimation() {
        supportInvalidateOptionsMenu();
        final View myView = edSearchStore;

        int cx = myView.getWidth();
        int cy = myView.getHeight() / 2;

        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, cx, 0);
        }

        if (anim != null) {
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            });

            anim.start();
        }

        rlContainer.setVisibility(View.VISIBLE);

        Utils.hideKeyboard(this);
    }

    private void showCartActivity() {
        startActivity(new Intent(this, CartActivity.class));
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

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        Log.e(TAG, "onPrepareOptionsMenu: ");
        MenuItem item = menu.findItem(R.id.action_cart);
        RelativeLayout rootView = (RelativeLayout) item.getActionView();
        TextView tv = (TextView) rootView.findViewById(R.id.tv_total_items);
        if (PreferenceUtils.getInstance(this).getUserCart().getCartItems().size() > 0){
            tv.setText(String.valueOf(PreferenceUtils.getInstance(this).getUserCart().getTotalItems()));
        } else tv.setVisibility(View.GONE);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCartActivity();
            }
        });

        MenuItem itemSearch = menu.findItem(R.id.action_search);
        ImageView iv = (ImageView) itemSearch.getActionView();
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.findItem(R.id.action_search).setVisible(false);
                menu.findItem(R.id.action_cart).setVisible(false);
                menu.findItem(R.id.action_watch_list).setVisible(false);
                showSearchBarWithAnimation();
            }
        });

        return true;
    }

//    public void setUpHomeFragment() {
//        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_container, new HomeFragment(), HomeFragment.class.getSimpleName());
////        fragmentTransaction.addToBackStack(HomeFragment.class.getSimpleName());
//        fragmentTransaction.commit();
//    }


//    public void showCategoriesFragment(int position) {
//        CategoriesFragment categoriesFragment = new CategoriesFragment();
//        Bundle argBundle = new Bundle();
//        argBundle.putInt(ShoprConstants.STORE_ID, position);
//        categoriesFragment.setArguments(argBundle);
//        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.frame_container, categoriesFragment, CategoriesFragment.class.getSimpleName());
//        fragmentTransaction.addToBackStack(CategoriesFragment.class.getSimpleName());
//        fragmentTransaction.commit();
//    }

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

//    public void showProductDetailFragment(int storeId, int categoryId, Product product) {
//        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
//        Bundle argBundle = new Bundle();
//        argBundle.putInt(ShoprConstants.STORE_ID, storeId);
//        argBundle.putInt(ShoprConstants.CATEGORY_ID, categoryId);
//        argBundle.putParcelable(ShoprConstants.PRODUCT_OBJ, product);
//        productDetailFragment.setArguments(argBundle);
//        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.frame_container, productDetailFragment, ProductDetailFragment.class.getSimpleName());
//        fragmentTransaction.addToBackStack(ProductDetailFragment.class.getSimpleName());
//        fragmentTransaction.commit();
//    }

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_location:
                manuallySelectLocation();
                break;
        }
    }

    private void manuallySelectLocation() {
        startActivityForResult(new Intent(HomeActivity.this, PlacesActivity.class), PlacesActivity.PLACE_SELECTED);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                setLocationName(place.getName() + "");
                fetchCategoriesAndStores(place);
            }
        } else if (requestCode == PlacesActivity.PLACE_SELECTED ){
            if (resultCode == RESULT_OK){
                String placeName = data.getStringExtra(PlacesActivity.PLACE_NAME);
                String placeId = data.getStringExtra(PlacesActivity.PLACE_ID);
                setLocationName(placeName + "");
                fetchCategoriesAndStores(placeId);
            } else if (resultCode == RESULT_CANCELED){
                getPlaces();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQ_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getPlaces();
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQ_CODE);
        }
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

    @Override
    public void onResponse(Call<PlaceWiseCategoriesStores> call, Response<PlaceWiseCategoriesStores> response) {
        if (response.isSuccessful() && response.code() == 200
                && ((PlaceWiseCategoriesStores) response.body()).getCategories() != null
                && ((PlaceWiseCategoriesStores) response.body()).getStores() != null) {
            placeWiseCategoriesStores = response.body();
            setupCategoriesAndStores(placeWiseCategoriesStores);
        } else {
            setupCategoriesAndStores(null);
            showShortToast("No Stores available at this Location");
        }
    }

    private void setupCategoriesAndStores(PlaceWiseCategoriesStores placeWiseCategoriesStores) {
        if (placeWiseCategoriesStores == null){
            mViewPager.setAdapter(null);
            mViewPager.invalidate();
        } else {
            if (placeWiseCategoriesStores.getCategories().size() < 4) {
                mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            }
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), placeWiseCategoriesStores);
            mViewPager.setAdapter(adapter);
        }
    }

    @Override
    public void onFailure(Call<PlaceWiseCategoriesStores> call, Throwable t) {
        Log.e(TAG, "onFailure: ", t);
    }

    public void showStoresDetailActivity(int storeId) {
        Intent intent = new Intent(HomeActivity.this, StoresDetailActivity.class);
        intent.putExtra(ShoprConstants.STORE_POJO, placeWiseCategoriesStores.getStoreById(storeId));
        intent.putExtra(ShoprConstants.STORE_LOCATION, mLocationName.getText() + "");
        startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            hideSearchBarWithAnimation();
        }
        sendTermToHomeFragment(s.toString().toLowerCase());

    }

    private void sendTermToHomeFragment(String s) {
        HomeFragment fragment = (HomeFragment) mFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + mViewPager.getCurrentItem());
        if (fragment != null) {
            fragment.searchWithTerm(s);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.hideKeyboard(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }
}
