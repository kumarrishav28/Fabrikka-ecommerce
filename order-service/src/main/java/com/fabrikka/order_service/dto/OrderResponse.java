package com.fabrikka.order_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private Long orderId;
    private Long userId;
    private LocalDateTime createdAt;
    private String status;
    private Double totalAmount;
    private List<OrderItemResponse> items;

    @Data
    @Builder
    public static class OrderItemResponse {
        private Long productId;
        private Integer quantity;
        private Double price;
    }
}
