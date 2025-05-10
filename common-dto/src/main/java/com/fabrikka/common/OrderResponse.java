package com.fabrikka.common;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
        private UUID productId;
        private Integer quantity;
        private Double price;
    }
}
