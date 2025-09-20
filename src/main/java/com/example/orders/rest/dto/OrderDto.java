package com.example.orders.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {
    private String id;
    private String customerId;
    private List<OrderItemDto> items = new ArrayList<>();
    private double totalAmount;
    private String status;
    private Long createdAt;
    private Long updatedAt;

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

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long c) {
        this.createdAt = c;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long u) {
        this.updatedAt = u;
    }
}
