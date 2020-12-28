package com.example.hardwarewale.bean;

import java.io.Serializable;

public class OrderItems implements Serializable {
    private String productId;
    private int qty;
    private String name;
    private double total;
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
        this.qty = quantity;
        this.name = productName;
        this.total = amount;
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

    public int getQty() {
        return qty;
    }

    public void setQty(int quantity) {
        this.qty = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String productName) {
        this.name = productName;
    }

    public double getToal() {
        return total;
    }

    public void setTotal(double amount) {
        this.total = amount;
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

