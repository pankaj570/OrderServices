package com.example.orders.mcptools.model;

import com.example.orders.domain.OrderEntity;


import java.util.stream.Collectors;

public class OrderMcpMapper {
    public static Order entityToMcpOrder(OrderEntity e) {
        Order order = new Order();
        order.setId(e.getId() != null ? e.getId().toString() : null);
        order.setCustomerId(e.getCustomerId());
        order.setStatus(e.getStatus());
        order.setTotalAmount(e.getTotalAmount());
        order.setCreatedAt(e.getCreatedAt() != null ? e.getCreatedAt().toEpochMilli() : null);
        order.setUpdatedAt(e.getUpdatedAt() != null ? e.getUpdatedAt().toEpochMilli() : null);
        order.setItems(e.getItems().stream().map(it -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(it.getProductId());
            orderItem.setQuantity(it.getQuantity());
            orderItem.setPrice(it.getPrice());
            return orderItem;
        }).collect(Collectors.toList()));
        return order;
    }
}
