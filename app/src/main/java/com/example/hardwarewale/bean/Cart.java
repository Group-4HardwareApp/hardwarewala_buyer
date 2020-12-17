
package com.example.hardwarewale.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Cart implements Serializable {

    @SerializedName("cartId")
    @Expose
    private String cartId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("shopKeeperId")
    @Expose
    private Object shopKeeperId;
    @SerializedName("qtyInStock")
    @Expose
    private Double qtyInStock;
    @SerializedName("totalAmt")
    @Expose
    private Double totalAmt;
    @SerializedName("qty")
    @Expose
    private Integer qty;


    public Cart() {
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getQtyInStock() {
        return qtyInStock;
    }

    public void setQtyInStock(Double qtyInStock) {
        this.qtyInStock = qtyInStock;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Cart(String cartId, String userId, String categoryId, String productId, String name, Double price, String brand, String imageUrl,
                String description, Object shopKeeperId, Double qtyInStock, Double totalAmt, Integer qty) {
        this.cartId = cartId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.imageUrl = imageUrl;
        this.description = description;
        this.shopKeeperId = shopKeeperId;
        this.qtyInStock = qtyInStock;
        this.totalAmt = totalAmt;
        this.qty = qty;
    }

    public Cart(String userId, String categoryId, String productId, String name,
                Double price, String brand, String imageUrl, String description, Object shopKeeperId) {
        super();
        this.userId = userId;
        this.categoryId = categoryId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.imageUrl = imageUrl;
        this.description = description;
        this.shopKeeperId = shopKeeperId;
    }


    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getShopKeeperId() {
        return shopKeeperId;
    }

    public void setShopKeeperId(Object shopKeeperId) {
        this.shopKeeperId = shopKeeperId;
    }

}
