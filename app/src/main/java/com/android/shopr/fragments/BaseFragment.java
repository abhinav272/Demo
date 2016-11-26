package com.android.shopr.fragments;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by abhinav.sharma on 11/26/2016.
 */

public class BaseFragment extends Fragment {

    public void showShortToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
