package com.android.shopr.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.shopr.R;
import com.android.shopr.model.Cart;
import com.android.shopr.model.CartItem;
import com.android.shopr.model.Product;
import com.android.shopr.model.ProductFromBarcode;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by abhinav.sharma on 18/01/17.
 */

public class Utils {

    private static final String TAG = "Utils";

    public static void clearAllDataAndRevokeAccess(final Context context) {
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                PreferenceUtils.getInstance(context).saveUserProfile(null);
            }
        });
        Log.d(TAG, "revokeAccess: ");
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
    }

    public static int getRandomBackgroundColor() {
        List<Integer> bgColors = Arrays.asList(0xFF550C18, 0xFF443730, 0xFF786452, 0xFFF7DAD9, 0xFFFF3864,
                0xFF261447, 0xFFB6DC76, 0xFF39304A, 0xFFEAFFDA, 0xFF363020, 0xFFD6E3F8, 0xFF9D5C63,
                0xFF584B53, 0xFFFEF5EF, 0xFF197BBD, 0xFFDC758F, 0xFF00FFCD, 0xFFE7E08B, 0xFFD78A76,
                0xFFA0DDE6, 0xFFE8D6CB, 0xFFD0ADA7, 0xFFAD6A6C, 0xFFB58DB6);
//        List bgColors = Arrays.asList(context.getResources().getStringArray(R.array.bg_colors));
        Collections.shuffle(bgColors);
        int color = bgColors.get(0);
        Log.d(TAG, "getRandomBackgroundColor: " + color);
        return color;
    }

    public static void addProductToCart(Context context, int storeId, int categoryId, String storeName, String storeLocation, Product product, int size, int quantity) {
        Cart cart = PreferenceUtils.getInstance(context).getUserCart();
        CartItem cartItem = getCartItemFromProduct(storeId, categoryId, storeName, storeLocation, product, size, quantity);
        if (cart != null) {
            List<CartItem> cartItems = cart.getCartItems();
            cart.setStoreNameAndAddress(cartItem.getStoreName() + ", " + cartItem.getLocationName());
            if (!cartItems.contains(cartItem)) {
                cartItems.add(cartItem);
            } else {
                int position = cartItems.indexOf(cartItem);
                CartItem cartItem1 = cartItems.get(position);
                int qty = cartItem.getProductQuantity();
                cartItem.setProductQuantity(cartItem1.getProductQuantity() + qty);
                cartItems.set(position, cartItem);
                double total = cart.getCartTotal();
                total -= cartItem1.getProductPriceAfterDiscount() * cartItem1.getProductQuantity();
                cart.setCartTotal(total);

            }
            cart.setCartItems(cartItems);
            double total = cart.getCartTotal();
            total += ((cartItem.getProductPriceAfterDiscount()) * cartItem.getProductQuantity());
            cart.setCartTotal(total);
        } else {
            cart = new Cart();
            cart.setStoreNameAndAddress(cartItem.getStoreName() + ", " + cartItem.getLocationName());
            cart.setCartTotal(cartItem.getProductPriceAfterDiscount() * cartItem.getProductQuantity());
            List<CartItem> cartItems = new ArrayList<>();
            cartItems.add(cartItem);
            cart.setCartItems(cartItems);
        }
        PreferenceUtils.getInstance(context).saveUserCart(cart);
        Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();
    }

    public static void addProductToCart(Context context, ProductFromBarcode productFromBarcode, int size, int quantity) {
        addProductToCart(context, productFromBarcode.getStoreId(), productFromBarcode.getCategoryId(),
                productFromBarcode.getStoreName(), "", productFromBarcode.getProduct(), size, quantity);
    }

    @NonNull
    private static CartItem getCartItemFromProduct(int storeId, int categoryId, String storeName, String storeLocation, Product product, int size, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setStoreId(storeId);
        cartItem.setCategoryId(categoryId);
        cartItem.setProductId(product.getProductId());
        cartItem.setProductName(product.getProductName());
        cartItem.setImgUrl(product.getImageUrl());
        cartItem.setDiscount(product.getDiscount());
        cartItem.setProductPriceAfterDiscount(Double.valueOf(product.getPriceAfterDiscount()));
        cartItem.setProductPriceBeforeDiscount(Double.valueOf(product.getPriceBeforeDiscount()));
        cartItem.setProductQuantity(quantity);
        cartItem.setStoreName(storeName);
        cartItem.setLocationName(storeLocation);
        cartItem.setSize(size);
        return cartItem;
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY); // show
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(activity.getWindow().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void toggleKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            hideKeyboard(activity);
        } else {
            showKeyboard(activity);
        }
    }
}
