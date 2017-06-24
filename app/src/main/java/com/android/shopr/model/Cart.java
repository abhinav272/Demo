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
        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem cartItem = iterator.next();
            if (cartItem != null && cartItem.getProductId() == productId) {
                iterator.remove();
                updateCartTotal();
            }
        }
    }

    public int getTotalItems() {
        int total = 0;
        for (CartItem cartItem :
                cartItems) {
            total += cartItem.getProductQuantity();
        }
        return total;
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        updateCartTotal();
    }

    public void updateCartTotal() {
        double total = 0.0;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getProductPriceAfterDiscount() * cartItem.getProductQuantity();
        }
        cartTotal = total;
    }

    public double getCartTotalBeforeDiscount() {
        double total = 0.0;
        for (CartItem c : cartItems) {
            if (c != null) {
                total += c.getProductPriceBeforeDiscount() * c.getProductQuantity();
            }
        }
        return total;
    }
}
