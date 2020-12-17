package com.example.hardwarewale.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class BuyCart implements Serializable {
    private ArrayList<Cart> cartList;
   /* private String name;
    private double qty;
    private double price;
    private double totalmt;
*/
    public BuyCart(){}

    /*public double getTotalmt() {
        return totalmt;
    }

    public void setTotalmt(double totalmt) {
        this.totalmt = totalmt;
    }

    public BuyCart(ArrayList<Cart> cartList, String name, double qty, double price) {
        this.cartList = cartList;
        this.name = name;
        this.qty = qty;
        this.price = price;
    }
*/
    public BuyCart(ArrayList<Cart> cartList) {
        this.cartList = cartList;
    }
/*
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
*/
    public ArrayList<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<Cart> cartList) {
        this.cartList = cartList;
    }
}
