package com.android.shopr.api;

import com.android.shopr.utils.ShoprConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by abhinav.sharma on 1/10/2017.
 */
public class ShoprAPIClient {

    private static Retrofit retrofit;
    private static APIInterface apiInterface;

    private ShoprAPIClient() {
    }

    private static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ShoprConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static APIInterface getApiInterface() {
        return getClient().create(APIInterface.class);
    }

}
