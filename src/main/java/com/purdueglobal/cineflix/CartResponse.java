package com.purdueglobal.cineflix;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {
    private List<CartItem> cartItems;
    private int totalItems;
    private BigDecimal totalPrice;

    public CartResponse(List<CartItem> cartItems2, int totalItems2, BigDecimal totalPrice2) {
        this.cartItems = cartItems2;
        this.totalItems = totalItems2;
        this.totalPrice = totalPrice2;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
