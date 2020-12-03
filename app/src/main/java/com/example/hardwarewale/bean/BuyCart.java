package com.example.hardwarewale.bean;

import java.util.ArrayList;

public class BuyCart {
   private ArrayList<Cart> cartList;

    public ArrayList<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<Cart> cartList) {
        this.cartList = cartList;
    }

    public BuyCart(ArrayList<Cart> cartList) {
        this.cartList = cartList;
    }
    public BuyCart(){}
}
