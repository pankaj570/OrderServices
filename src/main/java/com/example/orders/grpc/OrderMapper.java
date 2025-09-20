package com.example.orders.grpc;

import com.example.orders.domain.*;
import com.example.orders.grpc.model.Order;
import com.example.orders.grpc.model.OrderItem;


import java.time.Instant;
import java.util.UUID;

public class OrderMapper {

    public static Order entityToProto(OrderEntity e) {
        Order.Builder b = Order.newBuilder()
                .setId(e.getId() != null ? e.getId().toString() : "")
                .setCustomerId(e.getCustomerId() != null ? e.getCustomerId() : "")
                .setTotalAmount(e.getTotalAmount())
                .setStatus(e.getStatus() != null ? e.getStatus() : "")
                .setCreatedAt(e.getCreatedAt() != null ? e.getCreatedAt().toEpochMilli() : 0L)
                .setUpdatedAt(e.getUpdatedAt() != null ? e.getUpdatedAt().toEpochMilli() : 0L);
        for (OrderItemEntity it : e.getItems()) {
            b.addItems(OrderItem.newBuilder()
                    .setProductId(it.getProductId() == null ? "" : it.getProductId())
                    .setPrice(it.getPrice())
                    .setQuantity(it.getQuantity())
            );
        }
        return b.build();
    }

    public static OrderEntity protoToEntity(Order p) {
        OrderEntity e = new OrderEntity();
        if (p.getId() != null && !p.getId().isEmpty()) {
            e.setId(UUID.fromString(p.getId()));
        }
        e.setCustomerId(p.getCustomerId());
        e.setStatus(p.getStatus());
        e.getItems().clear();
        for (OrderItem it : p.getItemsList()) {
            OrderItemEntity ie = new OrderItemEntity();
            ie.setProductId(it.getProductId());
            ie.setPrice(it.getPrice());
            ie.setQuantity(it.getQuantity());
            e.getItems().add(ie);
        }
        long created = p.getCreatedAt();
        long updated = p.getUpdatedAt();
        e.setCreatedAt(created == 0 ? Instant.now() : Instant.ofEpochMilli(created));
        e.setUpdatedAt(updated == 0 ? Instant.now() : Instant.ofEpochMilli(updated));
        return e;
    }
}
