package com.example.hardwarewale.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class
OrderCart implements Serializable {

    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("deliveryAddress")
    @Expose
    private String deliveryAddress;
    @SerializedName("totalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName("contactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("deliveryOption")
    @Expose
    private String deliveryOption;
    @SerializedName("shippingStatus")
    @Expose
    private String shippingStatus;
    @SerializedName("paymentOption")
    @Expose
    private String paymentOption;
    @SerializedName("itemList")
    @Expose
    private List<Cart> orderItems = null;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;

    public OrderCart() {
    }

    public OrderCart(String orderId, String userId, String name, String date, String deliveryAddress,
                     Double totalAmount, String contactNumber, String deliveryOption, String shippingStatus,
                     String paymentOption, List<Cart> orderItems, Long timestamp) {
       // this.shopkeeperId = shopkeeperId;
        this.orderId = orderId;
        this.userId = userId;
        this.name = name;
        this.date = date;
        this.deliveryAddress = deliveryAddress;
        this.totalAmount = totalAmount;
        this.contactNumber = contactNumber;
        this.deliveryOption = deliveryOption;
        this.shippingStatus = shippingStatus;
        this.paymentOption = paymentOption;
        this.orderItems = orderItems;
        this.timestamp = timestamp;
    }

    public OrderCart(String userId, String name, String date, String deliveryAddress, Double totalAmount,
                     String contactNumber, String deliveryOption, String shippingStatus,
                     String paymentOption, List<Cart> orderItems, Long timestamp) {
        this.userId = userId;
        this.name = name;
        this.date = date;
        this.deliveryAddress = deliveryAddress;
        this.totalAmount = totalAmount;
        this.contactNumber = contactNumber;
        this.deliveryOption = deliveryOption;
        this.shippingStatus = shippingStatus;
        this.paymentOption = paymentOption;
        this.orderItems = orderItems;
        this.timestamp = timestamp;
    }

    public String getPaymentOption() { return paymentOption; }

    public void setPaymentOption(String paymentOption) { this.paymentOption = paymentOption; }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public List<Cart> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<Cart> orderItems) {
        this.orderItems = orderItems;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
