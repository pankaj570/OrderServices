package com.example.orders.graphql;

import com.example.orders.domain.OrderEntity;
import com.example.orders.domain.OrderItemEntity;
import com.example.orders.graphql.model.Order;
import com.example.orders.graphql.model.OrderGraphQLMapper;
import com.example.orders.service.OrderDomainService;
import com.example.orders.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class OrderGraphQLController {

    private final OrderDomainService orderService;

    public OrderGraphQLController(OrderDomainService orderService) {
        this.orderService = orderService;
    }

    // Queries
    @QueryMapping
    public Order getOrderById(@Argument String id) {
        Order order = null;
        Optional<OrderEntity> optionalOrderEntity = orderService.getById(UUID.fromString(id));
        if(optionalOrderEntity.isPresent()){
            order = OrderGraphQLMapper.entityToOrder(optionalOrderEntity.get());
        }
        return order;
    }

    @QueryMapping
    public List<Order> listOrdersByDate(@Argument String startDate, @Argument String endDate) {
        List<Order> orderList = new ArrayList<>();
        Page<OrderEntity> page = orderService.listByDateRange(Instant.ofEpochMilli(DateUtil.stringDateToEpoch(startDate)),
                Instant.ofEpochMilli(DateUtil.stringDateToEpoch(endDate)), PageRequest.of(0, 50));
        if (!page.isEmpty()) {
            page.stream().forEach(e -> {
                Order order = OrderGraphQLMapper.entityToOrder(e);
                orderList.add(order);
            });
        }
        return orderList;
    }

    // Mutations
    @MutationMapping
    public Order createOrder(@Argument String customerName, @Argument String product, @Argument int quantity, @Argument float price) {
        List<OrderItemEntity> listOrderItemEntity = new ArrayList<>();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomerId(customerName);
        orderEntity.setStatus("NEW");
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setProductId(product);
        orderItemEntity.setPrice(price);
        orderItemEntity.setQuantity(quantity);
        listOrderItemEntity.add(orderItemEntity);
        orderEntity.setItems(listOrderItemEntity);
        OrderEntity orderEntityResp = orderService.create(orderEntity);
        return OrderGraphQLMapper.entityToOrder(orderEntityResp);
    }

    @MutationMapping
    public Order updateOrder(@Argument String id, @Argument String customerName, @Argument String product, @Argument int quantity, @Argument float price) {
        List<OrderItemEntity> listOrderItemEntity = new ArrayList<>();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(UUID.fromString(id));
        orderEntity.setCustomerId(customerName);
        orderEntity.setStatus("UPDATED");
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setProductId(product);
        orderItemEntity.setPrice(price);
        orderItemEntity.setQuantity(quantity);
        listOrderItemEntity.add(orderItemEntity);
        orderEntity.setItems(listOrderItemEntity);
        OrderEntity orderEntityResp = orderService.update(orderEntity);
        return OrderGraphQLMapper.entityToOrder(orderEntityResp);
    }

    @MutationMapping
    public boolean deleteOrder(@Argument String id) {
        orderService.delete(UUID.fromString(id));
        return true;
    }
}
