package com.example.hardwarewale.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("shopkeeperId")
    @Expose
    private String shopkeeperId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("discount")
    @Expose
    private Double discount;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("qtyInStock")
    @Expose
    private Integer qtyInStock;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("secondImageUrl")
    @Expose
    private String secondImageUrl;
    @SerializedName("thirdImageurl")
    @Expose
    private String thirdImageurl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("totalAmt")
    @Expose
    private Double totalAmt;

    public Product(){

    }

    public Product(String productId, String categoryId, String shopkeeperId, String name, Double price,
                   Double discount, String brand, Double totalAmt,Integer qtyInStock, String imageUrl,
                   String secondImageUrl,String thirdImageurl,String description, Long timestamp, Integer qty) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.shopkeeperId = shopkeeperId;
        this.totalAmt = totalAmt;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.brand = brand;
        this.qtyInStock = qtyInStock;
        this.imageUrl = imageUrl;
        this.secondImageUrl=secondImageUrl;
        this.thirdImageurl=thirdImageurl;
        this.description = description;
        this.timestamp = timestamp;
        this.qty = qty;
    }

    public String getSecondImageUrl() {
        return secondImageUrl;
    }

    public void setSecondImageUrl(String secondImageUrl) {
        this.secondImageUrl = secondImageUrl;
    }

    public String getThirdImageurl() {
        return thirdImageurl;
    }

    public void setThirdImageurl(String thirdImageurl) {
        this.thirdImageurl = thirdImageurl;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getShopkeeperId() {
        return shopkeeperId;
    }

    public void setShopkeeperId(String shopkeeperId) {
        this.shopkeeperId = shopkeeperId;
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

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getQtyInStock() {
        return qtyInStock;
    }

    public void setQtyInStock(Integer qtyInStock) {
        this.qtyInStock = qtyInStock;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
