package com.fabrikka.order_service.service;

import com.fabrikka.common.CreateOrderRequest;
import com.fabrikka.common.OrderResponse;
import com.fabrikka.order_service.entity.Order;
import com.fabrikka.order_service.entity.OrderItem;
import com.fabrikka.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = Order.builder()
                .userId(request.getUserId())
                .createdAt(LocalDateTime.now())
                .status("PENDING")
                .totalAmount(calculateTotal(request))
                .build();

        List<OrderItem> orderItems = request.getItems().stream()
                .map(item -> OrderItem.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        return mapToOrderResponse(savedOrder);
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    private double calculateTotal(CreateOrderRequest request) {
        return request.getItems()
                .stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .items(order.getItems().stream()
                        .map(item -> OrderResponse.OrderItemResponse.builder()
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public void removeOrder(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders != null && !orders.isEmpty()) {
            orderRepository.deleteAll(orders);
        }
    }
}