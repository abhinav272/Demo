package com.android.shopr;

import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shopr.fragments.CategoriesFragment;
import com.android.shopr.fragments.HomeFragment;
import com.android.shopr.fragments.ProductsFragment;
import com.android.shopr.model.UserProfile;
import com.android.shopr.utils.ExecutorSupplier;
import com.android.shopr.utils.PreferenceUtils;
import com.android.shopr.utils.ShoprConstants;
import com.squareup.picasso.Picasso;

import java.util.Stack;

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View mHeaderView;
    private FragmentManager mFragmentManager;
    private Stack<String> mTitleStack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpViews();
        setUpHomeFragment();
    }

    private void setUpNavigationItems(UserProfile userProfile) {

        ImageView iv = (ImageView) mHeaderView.findViewById(R.id.iv_user_image);
        Picasso.with(this).load(userProfile.getPicUrl()).fit().centerInside().into(iv);
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
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        mDrawerToggle.setDrawerIndicatorEnabled(true);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
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

    private void setActionBarTitle() {
//        mTitle = title;
        if (!mTitleStack.empty())
            getSupportActionBar().setTitle(mTitleStack.peek());
    }

    public void popTitleStack() {
        if (!mTitleStack.empty()){
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
    protected void onStop() {
        super.onStop();
    }
}
