package com.example.orders.graphql.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String id;
    private String customerId;
    private List<OrderItem> items = new ArrayList<>();
    private double totalAmount;
    private String status;
    private String createdAt;
    private String updatedAt;

    // getters/setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String c) {
        this.customerId = c;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double t) {
        this.totalAmount = t;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String s) {
        this.status = s;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String c) {
        this.createdAt = c;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String u) {
        this.updatedAt = u;
    }
}
