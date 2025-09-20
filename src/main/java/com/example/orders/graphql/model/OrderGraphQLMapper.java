package com.example.orders.graphql.model;

import com.example.orders.domain.OrderEntity;
import com.example.orders.util.DateUtil;

import java.util.stream.Collectors;

public class OrderGraphQLMapper {
    public static Order entityToOrder(OrderEntity e) {
        Order order = new Order();
        order.setId(e.getId() != null ? e.getId().toString() : null);
        order.setCustomerId(e.getCustomerId());
        order.setStatus(e.getStatus());
        order.setTotalAmount(e.getTotalAmount());
        order.setCreatedAt(DateUtil.epochToStringDate(e.getCreatedAt() != null ? e.getCreatedAt().toEpochMilli() : null));
        order.setUpdatedAt(DateUtil.epochToStringDate(e.getUpdatedAt() != null ? e.getUpdatedAt().toEpochMilli() : null));
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
