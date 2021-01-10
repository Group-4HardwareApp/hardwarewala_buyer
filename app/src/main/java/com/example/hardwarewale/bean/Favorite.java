
package com.example.hardwarewale.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Favorite implements Serializable {

    @SerializedName("favoriteId")
    @Expose
    private String favoriteId;
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
    @SerializedName("shopkeeperId")
    @Expose
    private String shopkeeperId;

    public Favorite() {
    }

    public Favorite(String userId, String categoryId, String productId, String name, Double price, String brand, String imageUrl, String description, String shopKeeperId) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.imageUrl = imageUrl;
        this.description = description;
        this.shopkeeperId = shopKeeperId;
    }

    public Favorite(String favoriteId, String userId, String categoryId, String productId, String name, Double price, String brand, String imageUrl, String description, String shopKeeperId) {
        this.favoriteId = favoriteId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.imageUrl = imageUrl;
        this.description = description;
        this.shopkeeperId = shopKeeperId;
    }

    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
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

    public String getShopKeeperId() {
        return shopkeeperId;
    }

    public void setShopkeeperId(String shopKeeperId) {
        this.shopkeeperId = shopKeeperId;
    }

}
