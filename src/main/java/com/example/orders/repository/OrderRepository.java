package com.example.orders.repository;

import com.example.orders.domain.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Query("select o from OrderEntity o where o.createdAt between :from and :to order by o.createdAt desc")
    Page<OrderEntity> findByCreatedAtBetween(@Param("from") Instant from, @Param("to") Instant to, Pageable pageable);
}