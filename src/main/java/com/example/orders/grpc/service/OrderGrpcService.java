package com.example.orders.grpc.service;

import com.example.orders.grpc.OrderMapper;
import com.example.orders.grpc.model.*;
import com.example.orders.service.OrderDomainService;
import com.example.orders.domain.OrderEntity;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import org.springframework.data.domain.PageRequest;
import org.springframework.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.UUID;

@GrpcService
public class OrderGrpcService extends OrderServiceGrpc.OrderServiceImplBase {

    private final OrderDomainService domain;

    public OrderGrpcService(OrderDomainService domain) {
        this.domain = domain;
    }

    @Override
    public void createOrder(CreateOrderRequest request, StreamObserver<CreateOrderResponse> responseObserver) {
        try {
            Order proto = request.getOrder();
            OrderEntity entity = OrderMapper.protoToEntity(proto);
            OrderEntity saved = domain.create(entity);
            CreateOrderResponse resp = CreateOrderResponse.newBuilder()
                    .setOrder(OrderMapper.entityToProto(saved))
                    .build();
            responseObserver.onNext(resp);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getOrderById(GetOrderByIdRequest request, StreamObserver<GetOrderByIdResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getId());
            var opt = domain.getById(id);
            if (opt.isPresent()) {
                responseObserver.onNext(GetOrderByIdResponse.newBuilder()
                        .setOrder(OrderMapper.entityToProto(opt.get()))
                        .build());
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(Status.NOT_FOUND.withDescription("Order not found").asRuntimeException());
            }
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void listOrdersByDateRange(ListOrdersByDateRangeRequest request, StreamObserver<ListOrdersByDateRangeResponse> responseObserver) {
        try {
            Instant from = Instant.ofEpochMilli(request.getFrom());
            Instant to = Instant.ofEpochMilli(request.getTo());
            var page = domain.listByDateRange(from, to, PageRequest.of(request.getPage(), request.getSize()));
            ListOrdersByDateRangeResponse.Builder builder = ListOrdersByDateRangeResponse.newBuilder();
            page.forEach(o -> builder.addOrders(OrderMapper.entityToProto(o)));
            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void updateOrder(UpdateOrderRequest request, StreamObserver<UpdateOrderResponse> responseObserver) {
        try {
            Order proto = request.getOrder();
            OrderEntity entity = OrderMapper.protoToEntity(proto);
            OrderEntity updated = domain.update(entity);
            responseObserver.onNext(UpdateOrderResponse.newBuilder().setOrder(OrderMapper.entityToProto(updated)).build());
            responseObserver.onCompleted();
        } catch (IllegalArgumentException iae) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(iae.getMessage()).asRuntimeException());
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void deleteOrder(DeleteOrderRequest request, StreamObserver<DeleteOrderResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getId());
            domain.delete(id);
            responseObserver.onNext(DeleteOrderResponse.newBuilder().setSuccess(true).build());
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).asRuntimeException());
        }
    }
}
