package com.example.orders.rest;

import com.example.orders.domain.OrderEntity;
import com.example.orders.domain.OrderItemEntity;
import com.example.orders.rest.dto.OrderDto;
import com.example.orders.rest.dto.OrderItemDto;

import java.time.Instant;
import java.util.stream.Collectors;

public class OrderRestMapper {

    public static OrderDto entityToDto(OrderEntity e) {
        OrderDto dto = new OrderDto();
        dto.setId(e.getId() != null ? e.getId().toString() : null);
        dto.setCustomerId(e.getCustomerId());
        dto.setStatus(e.getStatus());
        dto.setTotalAmount(e.getTotalAmount());
        dto.setCreatedAt(e.getCreatedAt() != null ? e.getCreatedAt().toEpochMilli() : null);
        dto.setUpdatedAt(e.getUpdatedAt() != null ? e.getUpdatedAt().toEpochMilli() : null);
        dto.setItems(e.getItems().stream().map(it -> {
            OrderItemDto idto = new OrderItemDto();
            idto.setProductId(it.getProductId());
            idto.setQuantity(it.getQuantity());
            idto.setPrice(it.getPrice());
            return idto;
        }).collect(Collectors.toList()));
        return dto;
    }

    public static OrderEntity dtoToEntity(OrderDto dto) {
        OrderEntity e = new OrderEntity();
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            e.setId(java.util.UUID.fromString(dto.getId()));
        }
        e.setCustomerId(dto.getCustomerId());
        e.setStatus(dto.getStatus());
        e.getItems().clear();
        if (dto.getItems() != null) {
            dto.getItems().forEach(i -> {
                OrderItemEntity ie = new OrderItemEntity();
                ie.setProductId(i.getProductId());
                ie.setPrice(i.getPrice());
                ie.setQuantity(i.getQuantity());
                e.getItems().add(ie);
            });
        }
        if (dto.getCreatedAt() != null) e.setCreatedAt(Instant.ofEpochMilli(dto.getCreatedAt()));
        if (dto.getUpdatedAt() != null) e.setUpdatedAt(Instant.ofEpochMilli(dto.getUpdatedAt()));
        return e;
    }
}
