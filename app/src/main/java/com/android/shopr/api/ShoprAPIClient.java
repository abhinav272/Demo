package com.android.shopr.api;

import com.android.shopr.utils.NullOnEmptyConverterFactory;
import com.android.shopr.utils.ShoprConstants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
                    .client(getOkHttpClient())
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        return client;
    }

    public static APIInterface getApiInterface() {
        return getClient().create(APIInterface.class);
    }

}
