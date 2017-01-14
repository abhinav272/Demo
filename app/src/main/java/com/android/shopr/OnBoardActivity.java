package com.android.shopr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.android.shopr.fragments.LoginFragment;
import com.android.shopr.fragments.SplashFragment;

/**
 * Created by abhinav.sharma on 11/26/2016.
 */

public class OnBoardActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        showSplashFragment();

//        Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                showLoginFragment();
//            }
//        });
    }

    public void showLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new LoginFragment(), LoginFragment.class.getSimpleName());
        transaction.commit();
    }

    private void showSplashFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .add(R.id.frame_container, new SplashFragment(), SplashFragment.class.getSimpleName());
        transaction.commit();
    }

    public void showHomeActivity(){
        startActivity(new Intent(OnBoardActivity.this, HomeActivity.class));
        finish();
    }
}
