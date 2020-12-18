package com.example.hardwarewale.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class BuyCart implements Serializable {

    private ArrayList<Cart> cartList;

    public BuyCart() {
    }

    public BuyCart(ArrayList<Cart> cartList) {
        this.cartList = cartList;
    }

    public ArrayList<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<Cart> cartList) {
        this.cartList = cartList;
    }
}
