package com.android.shopr.api;

import com.android.shopr.api.reponse.GenericResponse;
import com.android.shopr.utils.ShoprConstants;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by abhinav.sharma on 1/8/2017.
 */

public interface APIInterface {


    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
    })
    @FormUrlEncoded
    @POST("/login")
    Call<GenericResponse> login(@Field("email") String email,
                                @Field("user_name") String userName,
                                @Field("access_token") String accessToken);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
    })
    @FormUrlEncoded
    @POST("/check")
    Call<GenericResponse> check(@Field("email") String email,
                                @Field("access_token") String accessToken);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
    })
    @FormUrlEncoded
    @POST("/logout")
    Call<GenericResponse> logout(@Field("access_token") String accessToken);


}
