package com.example.orders.service;

import com.example.orders.domain.OrderEntity;
import com.example.orders.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderDomainService {

    private final OrderRepository repository;

    public OrderDomainService(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public OrderEntity create(OrderEntity e) {
        if (e.getId() == null) e.setId(UUID.randomUUID());
        Instant now = Instant.now();
        e.setCreatedAt(now);
        e.setUpdatedAt(now);
        double total = e.getItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        e.setTotalAmount(total);
        return repository.save(e);
    }

    public Optional<OrderEntity> getById(UUID id) {
        return repository.findById(id);
    }

    public Page<OrderEntity> listByDateRange(Instant from, Instant to, Pageable page) {
        return repository.findByCreatedAtBetween(from, to, page);
    }

    @Transactional
    public OrderEntity update(OrderEntity e) {
        var opt = repository.findById(e.getId());
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Order not found: " + e.getId());
        }
        OrderEntity existing = opt.get();
        existing.setCustomerId(e.getCustomerId());
        existing.setStatus(e.getStatus());
        existing.getItems().clear();
        existing.getItems().addAll(e.getItems());
        existing.setTotalAmount(existing.getItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum());
        existing.setUpdatedAt(Instant.now());
        return repository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
