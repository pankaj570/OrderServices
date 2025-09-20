package com.example.orders.rest;

import com.example.orders.rest.dto.OrderDto;
import com.example.orders.service.OrderDomainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderDomainService domain;

    public OrderRestController(OrderDomainService domain) {
        this.domain = domain;
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody OrderDto dto) {
        var entity = OrderRestMapper.dtoToEntity(dto);
        var saved = domain.create(entity);
        return ResponseEntity.ok(OrderRestMapper.entityToDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> get(@PathVariable String id) {
        var opt = domain.getById(UUID.fromString(id));
        return opt.map(e -> ResponseEntity.ok(OrderRestMapper.entityToDto(e)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> list(
            @RequestParam long from,
            @RequestParam long to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        var pg = domain.listByDateRange(Instant.ofEpochMilli(from), Instant.ofEpochMilli(to), PageRequest.of(page, size));
        var list = pg.getContent().stream().map(OrderRestMapper::entityToDto).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> update(@PathVariable String id, @RequestBody OrderDto dto) {
        dto.setId(id);
        var entity = OrderRestMapper.dtoToEntity(dto);
        var updated = domain.update(entity);
        return ResponseEntity.ok(OrderRestMapper.entityToDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        domain.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
