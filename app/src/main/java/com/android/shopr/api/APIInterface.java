package com.android.shopr.api;

import com.android.shopr.model.CategoryWiseProducts;
import com.android.shopr.model.GenericResponse;
import com.android.shopr.model.PlaceWiseCategoriesStores;
import com.android.shopr.model.ProductFromBarcode;
import com.android.shopr.model.Store;
import com.android.shopr.model.StoreWiseCategory;

import retrofit2.Call;
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
    @POST("auth/login")
    Call<GenericResponse> login(@Field("email") String email,
                                @Field("user_name") String userName,
                                @Field("access_token") String accessToken);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
    })
    @FormUrlEncoded
    @POST("auth/check")
    Call<GenericResponse> check(@Field("email") String email,
                                @Field("access_token") String accessToken);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
    })
    @FormUrlEncoded
    @POST("auth/logout")
    Call<GenericResponse> logout(@Field("access_token") String accessToken);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
    })
    @FormUrlEncoded
    @POST("categories/all")
    Call<StoreWiseCategory> getStoreWiseCategories(@Field("store_id") int storeId);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
    })
    @FormUrlEncoded
    @POST("stores/all")
    Call<Store.List> getAllStores(@Field("none") String emptyBody);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
    })
    @FormUrlEncoded
    @POST("products/all")
    Call<CategoryWiseProducts> getCategorySpecificProducts(@Field("store_id") int store_id, @Field("category_id") int categoryId);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
    })
    @FormUrlEncoded
    @POST("stores/storesByPlace")
    Call<PlaceWiseCategoriesStores> getPlaceWiseCategoriesStores(@Field("googleId") String googlePlaceId);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
    })
    @FormUrlEncoded
    @POST("products/barcode")
    Call<ProductFromBarcode> getProductFromBarcode(@Field("barcode") String barcodeId);


}
