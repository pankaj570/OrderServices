package com.example.orders.mcptools;

import com.example.orders.domain.OrderEntity;
import com.example.orders.domain.OrderItemEntity;
import com.example.orders.mcptools.model.Order;
import com.example.orders.mcptools.model.OrderMcpMapper;
import com.example.orders.service.OrderDomainService;
import com.example.orders.util.DateUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderMcpTool {

    private final OrderDomainService orderService;

    public OrderMcpTool(OrderDomainService orderService) {
        this.orderService = orderService;
    }

    @Tool(name = "create_order", description = "Create a new order")
    public Order createOrder(String customerName, String product, int quantity, double price) {
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
        return OrderMcpMapper.entityToMcpOrder(orderEntityResp);
    }

    @Tool(name = "get_recent_orders", description = "Get list of recent orders by date range and date format like yyyy-MM-dd")
    public List<Order> getRecentOrders(String startDate, String endDate) {
        List<Order> orderList = new ArrayList<>();
        Page<OrderEntity> page = orderService.listByDateRange(Instant.ofEpochMilli(DateUtil.stringDateToEpoch(startDate)),
                Instant.ofEpochMilli(DateUtil.stringDateToEpoch(endDate)), PageRequest.of(0, 50));
        if (!page.isEmpty()) {
            page.stream().forEach(e -> {
                Order order = OrderMcpMapper.entityToMcpOrder(e);
                orderList.add(order);
            });
        }
        return orderList;
    }

    @Tool(name = "get_order_by_id", description = "Get order by ID")
    public Order getOrderById(String id) {
        Order order = null;
        Optional<OrderEntity> optionalOrderEntity = orderService.getById(UUID.fromString(id));
        if(optionalOrderEntity.isPresent()){
            order = OrderMcpMapper.entityToMcpOrder(optionalOrderEntity.get());
        }
        return order;
    }

    @Tool(name = "update_order", description = "Update order info")
    public Order updateOrder(String id, String customerName, String product, int quantity, double price) {
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
        return OrderMcpMapper.entityToMcpOrder(orderEntityResp);
    }

    @Tool(name = "delete_order", description = "Delete order by ID")
    public String deleteOrder(String id) {
        orderService.delete(UUID.fromString(id));
        return "Order with ID " + id + " deleted successfully";
    }
}
