package com.fabrikka.order_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    private Long userId;
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
        private Double price;
    }
}