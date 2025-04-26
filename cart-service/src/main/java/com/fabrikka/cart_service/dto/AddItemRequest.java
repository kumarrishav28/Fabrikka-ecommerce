package com.fabrikka.cart_service.dto;


import lombok.Data;

@Data
public class AddItemRequest {
    private Long productId;
    private Integer quantity;
}