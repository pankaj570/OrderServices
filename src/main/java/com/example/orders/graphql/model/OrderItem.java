package com.example.orders.graphql.model;

public class OrderItem {
    private String productId;
    private int quantity;
    private double price;

    // getters/setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String p) {
        this.productId = p;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int q) {
        this.quantity = q;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double p) {
        this.price = p;
    }
}
