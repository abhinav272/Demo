package com.android.shopr.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.shopr.OnBoardActivity;
import com.android.shopr.R;
import com.android.shopr.api.ShoprAPIClient;
import com.android.shopr.api.reponse.GenericResponse;
import com.android.shopr.model.UserProfile;
import com.android.shopr.utils.ExecutorSupplier;
import com.android.shopr.utils.PreferenceUtils;
import com.android.shopr.utils.ShoprConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by abhinav.sharma on 11/26/2016.
 */

public class SplashFragment extends BaseFragment implements Callback<GenericResponse> {

    private static final String TAG = "SplashFragment";
    private UserProfile userProfile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkIfLoginNeeded();
    }

    private void checkIfLoginNeeded() {
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                userProfile = PreferenceUtils.getInstance(getActivity()).getUserProfile();
                if (userProfile != null) {
                    ExecutorSupplier.getInstance().getMainThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            Call<GenericResponse> call = ShoprAPIClient.getApiInterface().check(userProfile.getEmailId(), userProfile.getAccessToken());
                            call.enqueue(SplashFragment.this);
                        }
                    });
                } else {
                    ExecutorSupplier.getInstance().getMainThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            ((OnBoardActivity) getActivity()).showLoginFragment();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
        if (response.body().getResponse().equalsIgnoreCase(ShoprConstants.USER_ALREADY_LOGGED_IN))
            ((OnBoardActivity) getActivity()).showHomeActivity();
        else if(response.body().getResponse().equalsIgnoreCase(ShoprConstants.USER_ALREADY_LOGGED_OUT))
            ((OnBoardActivity) getActivity()).showLoginFragment();
    }

    @Override
    public void onFailure(Call<GenericResponse> call, Throwable t) {
        Log.e(TAG, "onFailure: ", t);
    }
}
