package com.example.hardwarewale.bean;

import java.io.Serializable;

public class OrderItems implements Serializable {
    private String productId;
    private int quantity;
    private String productName;
    private double amount;
    private String imageUrl;
    private double price;
    private String shopkeeperId;
    private String orderItemId;

    public OrderItems() {

    }

    public OrderItems(String productId, int quantity, String productName, double amount, String imageUrl,
                      double price, String shopkeeperId) {
        super();
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.amount = amount;
        this.imageUrl = imageUrl;
        this.price = price;
        this.shopkeeperId = shopkeeperId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getProductId() {
        return productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getShopkeeperId() {
        return shopkeeperId;
    }

    public void setShopkeeperId(String shopkeeperId) {
        this.shopkeeperId = shopkeeperId;
    }
}

