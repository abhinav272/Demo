package com.android.shopr.model;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Abhinav on 02/04/17.
 */
public class Cart {

    private String storeNameAndAddress;
    private double cartTotal;
    private List<CartItem> cartItems;

    public String getStoreNameAndAddress() {
        return storeNameAndAddress;
    }

    public void setStoreNameAndAddress(String storeNameAndAddress) {
        this.storeNameAndAddress = storeNameAndAddress;
    }

    public double getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(double cartTotal) {
        this.cartTotal = cartTotal;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void removeProductById(int productId) {
        for (Iterator<CartItem> iterator = cartItems.iterator(); iterator.hasNext();){
            CartItem cartItem = iterator.next();
            if (cartItem.getProductId() == productId)
                iterator.remove();
        }
    }
}
